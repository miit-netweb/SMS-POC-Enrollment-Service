package Microservices.Enrollment_Service.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import Microservices.Enrollment_Service.Entity.Subscriber;
import Microservices.Enrollment_Service.Service.AuthService;
import Microservices.Enrollment_Service.exception.ErrorCodes;
import Microservices.Enrollment_Service.exception.UnAuthorizedUserException;
import Microservices.Enrollment_Service.exception.ValidationException;
import Microservices.Enrollment_Service.proxy.JwtServerProxy;
import Microservices.Enrollment_Service.proxy.PartnerServiceProxy;
import Microservices.Enrollment_Service.proxy.ThirdPartyEntityProxy;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/v3/enroll")
public class V3EnrollmentController {

	private static final Logger LOGGER = LoggerFactory.getLogger(V3EnrollmentController.class);
	private AuthService service;
	private JwtServerProxy jwtServerProxy;
	private PartnerServiceProxy partnerProxy;
	private ThirdPartyEntityProxy thirdPartyProxy;
	

	@RateLimiter(name = "enrollmentService")
	@PostMapping("/subscriber")
	public ResponseEntity<?> addNewUser(@RequestBody SubscriberDto user,
	                                    @RequestHeader(value = "Authorization", required = false) String token) throws Exception{
	    LOGGER.info("Started validating user");

	    final ResponseEntity<?> responseEntity;
		try {
			// Token Service Call to validate Token
			responseEntity = jwtServerProxy.validateToken(token);
		} catch (RuntimeException ex) {
			LOGGER.error("Original exception: {}", ex.getMessage());
			throw new ValidationException(401, "Invalid Token",HttpStatus.BAD_REQUEST);
		}
		
	    if (!service.isValidPartner(responseEntity, user)) {
	    	LOGGER.error("Authentication Failed");
	        throw new UnAuthorizedUserException(4445, "Token does not belong to the specified partner", HttpStatus.UNAUTHORIZED);
	    }

	    if (!service.ValidateResponse(user)) {
	    	LOGGER.error("Validation failed for user");
	        throw new ValidationException(ErrorCodes.USER_VALIDATION_FAILED.getErrorCode(),
	                ErrorCodes.USER_VALIDATION_FAILED.getErrorMessage(), HttpStatus.BAD_REQUEST);
	    }

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
		
	    final String SUBSCRIBER_NUMBER = AuthService.generateRandomAlphaNumeric();
		LOGGER.info("Generated Subscriber Number {}", SUBSCRIBER_NUMBER);

	    ResponseEntity<Boolean> thirdPartyResponse = thirdPartyProxy.createCustomer(
				new ThirdPartyEntityDto(user.getEnrollmentDetail().getPartnerNumber(), SUBSCRIBER_NUMBER,
						user.getEnrollmentDetail().getSubscriptionData().getSubtypeNumber()));
	    
	    if (Boolean.FALSE.equals(thirdPartyResponse.getBody())) {
	        throw new ValidationException(ErrorCodes.SUBSCRIBER_CREATION_FAILED.getErrorCode(),
	                ErrorCodes.SUBSCRIBER_CREATION_FAILED.getErrorMessage(), HttpStatus.BAD_REQUEST);
	    }
	    Subscriber enrolledSubscriber = service.enrollNewSubscriber(user, SUBSCRIBER_NUMBER);
	    LOGGER.info("Subscriber Inserted into Subscriber table with Subscriber Number{}",SUBSCRIBER_NUMBER);

	    service.submitBillingPendingEntry(enrolledSubscriber, subscriptionData.getBody());
	    service.submitEmailPendingEntry(enrolledSubscriber);

	    LOGGER.info("returning response entity");
	    return ResponseEntity.ok("success");
	}
	
}
