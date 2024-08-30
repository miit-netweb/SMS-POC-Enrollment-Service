package Microservices.Enrollment_Service.Controller;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Microservices.Enrollment_Service.Dto.ExceptionResponse;
import Microservices.Enrollment_Service.Dto.PartnerServiceDto;
import Microservices.Enrollment_Service.Dto.SubscriberDto;
import Microservices.Enrollment_Service.Dto.SubscriptionData;
import Microservices.Enrollment_Service.Dto.ThirdPartyEntityDto;
import Microservices.Enrollment_Service.Entity.BillingPending;
import Microservices.Enrollment_Service.Entity.EmailPending;
import Microservices.Enrollment_Service.Entity.Subscriber;
import Microservices.Enrollment_Service.Publisher.BillingProducer;
import Microservices.Enrollment_Service.Publisher.RabbitMQProducer;
import Microservices.Enrollment_Service.Service.AuthService;
import Microservices.Enrollment_Service.Service.BillingService;
import Microservices.Enrollment_Service.Service.EmailService;
import Microservices.Enrollment_Service.exception.ErrorCodes;
import Microservices.Enrollment_Service.exception.UnAuthorizedUserException;
import Microservices.Enrollment_Service.exception.ValidationException;
import Microservices.Enrollment_Service.proxy.JwtServerProxy;
import Microservices.Enrollment_Service.proxy.PartnerServiceProxy;
import Microservices.Enrollment_Service.proxy.ThirdPartyEntityProxy;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@RestController
@RequestMapping("/v2/enroll")
public class V2EnrollmentController {

	private static final Logger LOGGER = LoggerFactory.getLogger(V2EnrollmentController.class);

	@Autowired
	private AuthService service;
	@Autowired
	private JwtServerProxy jwtServerProxy;
	@Autowired
	private PartnerServiceProxy partnerProxy;
	@Autowired
	private ThirdPartyEntityProxy thirdPartyProxy;
	@Autowired
	private RabbitMQProducer rabbitMQProducer;
	@Autowired
	private BillingProducer billingProducer;
	@Autowired
	private EmailService emailService;
	@Autowired
	private BillingService billingService;

	@RateLimiter(name = "enrollmentService")
	@PostMapping("/subscriber")
	public ResponseEntity<?> addNewUser(@RequestBody SubscriberDto user,
			@RequestHeader(value = "Authorization", required = false) String token) throws Exception {
		LOGGER.info("Started validating user");

		final ResponseEntity<?> responseEntity;
		try {
			// Token Service Call to validate Token
			responseEntity = jwtServerProxy.validateToken(token);
		} catch (RuntimeException ex) {
			LOGGER.error("Original exception: {}", ex.getMessage());
			throw new ValidationException(401, "Invalid Token",HttpStatus.BAD_REQUEST);
		}
         //Claims Check to verify token with partner number
		Object body = responseEntity.getBody();
		Map<String, Object> responseBody = (Map<String, Object>) body;
		String partnerNumber = Optional.ofNullable(responseBody).map(map -> (String) map.get("partnerNumber"))
				.orElse(null);
		Long partnerNumberFromUser = user.getEnrollmentDetail().getPartnerNumber();
		if (partnerNumber != null && partnerNumber.equals(partnerNumberFromUser.toString())) {
			//validating user details
			if (service.ValidateResponse(user)) {
				LOGGER.info("Validation successful for user of partner number-> {}",
						user.getEnrollmentDetail().getPartnerNumber());

				final ResponseEntity<SubscriptionData> subscriptionData;
				try {
					// Partner Service Call for checking subtype details
					subscriptionData = partnerProxy.validatePartner(
							new PartnerServiceDto(user.getEnrollmentDetail().getSubscriptionData()),
							user.getEnrollmentDetail().getPartnerNumber());

				} catch (RuntimeException ex) {
					LOGGER.error("Original exception: {}", ex.getMessage());
					ExceptionResponse errorResponse = ExceptionResponse.fromJson(ex.getMessage());
					throw new ValidationException(errorResponse.getErrorcode(), errorResponse.getMessage(),
							errorResponse.getStatus());
				}

				if (subscriptionData.getBody() != null) {
					LOGGER.info("partner number-> {} validated successfully",
							user.getEnrollmentDetail().getPartnerNumber());

					// Subscriber Number Generated
					final String SUBSCRIBER_NUMBER = AuthService.generateRandomAlphaNumeric();
					LOGGER.info("Generated Subscriber Number {}", SUBSCRIBER_NUMBER);

					//Third Party entity call
					ResponseEntity<Boolean> thirdPartyResponse = thirdPartyProxy.createCustomer(
							new ThirdPartyEntityDto(user.getEnrollmentDetail().getPartnerNumber(), SUBSCRIBER_NUMBER,
									user.getEnrollmentDetail().getSubscriptionData().getSubtypeNumber()));

					if (Boolean.TRUE.equals(thirdPartyResponse.getBody())) {
						final Subscriber ENROLLED_SUBSCRIBER = service.enrollNewSubscriber(user, SUBSCRIBER_NUMBER);

//================================================================================================
//===========================    BILLING SERVICE CALL   (1)  ===============================================
						// ADD in Billing Pending Table AND KAFKA :
						CompletableFuture.runAsync(() -> {
							BillingPending billingPending = new BillingPending(SUBSCRIBER_NUMBER,
									user.getEnrollmentDetail().getPartnerNumber(),
									subscriptionData.getBody().getSubtypeNumber(),
									subscriptionData.getBody().getPricingRoutine(), "BILLING-PENDING");
							billingService.saveBillingPendingEntry(billingPending);
							billingProducer.sendMessage(billingPending);
						});

//=================================   ENDED  =========================================================
//====================================================================================================

//================================================================================================
//===========================    EMAIL SERVICE  CALL   (2) ===============================================
						CompletableFuture.runAsync(() -> {
							EmailPending emailPending = emailService.addPendingEntry(new EmailPending(SUBSCRIBER_NUMBER,
									user.getEnrollmentDetail().getSubscriberData().getEmail(), 800, "EMAIL_PENDING"));
							rabbitMQProducer.sendMessage(emailPending);
						});
						LOGGER.info("returning response entity");
//=================================   ENDED  =========================================================
//====================================================================================================

						return ResponseEntity.ok("success");
					} else {
						LOGGER.error("Failed to create Subscriber at Third Party Entity with Subscriber Number->{}",
								SUBSCRIBER_NUMBER);
						throw new ValidationException(ErrorCodes.SUBSCRIBER_CREATION_FAILED.getErrorCode(),
								ErrorCodes.SUBSCRIBER_CREATION_FAILED.getErrorMessage(), HttpStatus.BAD_REQUEST);
					}
				} else {
					LOGGER.error("Invalid partner number-> {} for user enrollment",
							user.getEnrollmentDetail().getPartnerNumber());
					throw new ValidationException(ErrorCodes.INVALID_PARTNER.getErrorCode(),
							ErrorCodes.INVALID_PARTNER.getErrorMessage(), HttpStatus.BAD_REQUEST);
				}
			} else {
				LOGGER.error("Validation failed for user");
				throw new ValidationException(ErrorCodes.USER_VALIDATION_FAILED.getErrorCode(),
						ErrorCodes.USER_VALIDATION_FAILED.getErrorMessage(), HttpStatus.BAD_REQUEST);
			}
		} else {
			LOGGER.error("Authentication Failed");
			throw new UnAuthorizedUserException(4445, "Token does not belong to the specified partner",
					HttpStatus.UNAUTHORIZED);
		}
	}
}
