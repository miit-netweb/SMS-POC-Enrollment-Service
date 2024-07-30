package Microservices.Enrollment_Service.Controller;

import java.util.concurrent.CompletableFuture;

import Microservices.Enrollment_Service.Dto.*;
import Microservices.Enrollment_Service.Publisher.BillingProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Microservices.Enrollment_Service.Publisher.RabbitMQProducer;
import Microservices.Enrollment_Service.Service.AuthService;
import Microservices.Enrollment_Service.proxy.JwtServerProxy;
import Microservices.Enrollment_Service.proxy.PartnerServiceProxy;
import Microservices.Enrollment_Service.proxy.ThirdPartyEntityProxy;

@RestController
@RequestMapping("/enroll")
public class EnrollmentController {

	private static final Logger LOGGER = LoggerFactory.getLogger(EnrollmentController.class);

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


	@PostMapping("/subscriber")
	public ResponseEntity<?> addNewUser(@RequestBody SubscriberDto user) {
		LOGGER.info("Started validating user");
		if (service.ValidateResponse(user) == true) {
			LOGGER.info("Validation successful for user of partner number-> {}",
					user.getEnrollmentDetail().getPartnerNumber());

			ResponseEntity<SubscriptionData> subscriptionData = partnerProxy.validatePartner(
					new PartnerServiceDto(user.getPartnerCredential(),
							user.getEnrollmentDetail().getSubscriptionData()),
					user.getEnrollmentDetail().getPartnerNumber());
			System.out.println(subscriptionData);
			if (subscriptionData.getBody() != null) {
				LOGGER.info("partner number-> {} validated successfully",
						user.getEnrollmentDetail().getPartnerNumber());
				ResponseEntity<?> tokenGenerationResponseDto = jwtServerProxy
						.tokenGenerationForPartner(user.getEnrollmentDetail().getPartnerNumber());
				LOGGER.info("JWT Token generated successfully for user of partner number-> {}",
						user.getEnrollmentDetail().getPartnerNumber());
				// Subscriber Number Generated
				final String SUBSCRIBER_NUMBER = service.generateRandomAlphaNumeric();
				LOGGER.info("Generated Subscriber Number {}", SUBSCRIBER_NUMBER);

				ResponseEntity<Boolean> thirdPartyResponse = thirdPartyProxy.createCustomer(
						new ThirdPartyEntityDto(user.getEnrollmentDetail().getPartnerNumber(), SUBSCRIBER_NUMBER,
								user.getEnrollmentDetail().getSubscriptionData().getSubtypeNumber()));


				if (thirdPartyResponse.getBody() == true) {

//================================================================================================
//===========================    EMAIL SERVICE     (2) ===============================================
					EmailMessageDto emailMessageDto = new EmailMessageDto(
							user.getEnrollmentDetail().getSubscriberData().getEmail(),
							user.getEnrollmentDetail().getSubscriberData().getFirstName(),
							user.getEnrollmentDetail().getSubscriberData().getLastName(), 99995555L, 800);
					CompletableFuture.runAsync(() -> rabbitMQProducer.sendMessage(emailMessageDto));
					LOGGER.info("returning response entity");
//=================================   ENDED  =========================================================
//====================================================================================================

//================================================================================================
//===========================    BILLING SERVICE    (1)  ===============================================

					// ADD in Billing Pending Table

					// KAFKA :

					CompletableFuture.runAsync(()-> billingProducer.sendMessage(new SubscriptionBillingDto(SUBSCRIBER_NUMBER,user.getEnrollmentDetail().getPartnerNumber(),user.getEnrollmentDetail().getSubscriberData().getBillingDetail().getCardDetail(), subscriptionData.getBody())));

//=================================   ENDED  =========================================================
//====================================================================================================


					return ResponseEntity.ok(tokenGenerationResponseDto);
				} else {
					LOGGER.error("Failed to create Subscriber at Third Party Entity with Subscriber Number->{}",
							SUBSCRIBER_NUMBER);
					return ResponseEntity.badRequest().body("Failed to create Subscriber at Third Party Entity");
				}
			} else {
				LOGGER.error("Invalid partner number-> {} for user enrollment",
						user.getEnrollmentDetail().getPartnerNumber());

				// Todo add exception handling and error response
				return ResponseEntity.badRequest().body("Partner Validation failed");
			}
		} else {
			LOGGER.error("Validation failed for user");
			return ResponseEntity.badRequest().body(service.ValidateResponse(user));
		}
	}

}