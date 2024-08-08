package Microservices.Enrollment_Service.Controller;

import Microservices.Enrollment_Service.Dto.*;
import Microservices.Enrollment_Service.Entity.BillingPending;
import Microservices.Enrollment_Service.Entity.EmailPending;
import Microservices.Enrollment_Service.Entity.Subscriber;
import Microservices.Enrollment_Service.Publisher.BillingProducer;
import Microservices.Enrollment_Service.Publisher.RabbitMQProducer;
import Microservices.Enrollment_Service.Service.AuthService;
import Microservices.Enrollment_Service.Service.BillingService;
import Microservices.Enrollment_Service.Service.EmailService;
import Microservices.Enrollment_Service.exception.ValidationException;
import Microservices.Enrollment_Service.proxy.JwtServerProxy;
import Microservices.Enrollment_Service.proxy.PartnerServiceProxy;
import Microservices.Enrollment_Service.proxy.ThirdPartyEntityProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;


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


    @PostMapping("/subscriber")
    public ResponseEntity<?> addNewUser(@RequestBody SubscriberDto user, @RequestHeader(value = "Authorization", required = false) String token) throws Exception {
        LOGGER.info("Started validating user");

        LOGGER.info("Validating token for the partner number -> {}",user.getEnrollmentDetail().getPartnerNumber());
        ResponseEntity<?> responseEntity = jwtServerProxy.validateToken(token);
        LOGGER.info("Token validation successful for partner number -> {}",user.getEnrollmentDetail().getPartnerNumber());

        if (service.ValidateResponse(user)) {
            LOGGER.info("Validation successful for user of partner number-> {}",
                    user.getEnrollmentDetail().getPartnerNumber());

         final ResponseEntity<SubscriptionData> subscriptionData;
			try {
                LOGGER.info("Started validation subtype for partner number -> {}",user.getEnrollmentDetail().getPartnerNumber());
			subscriptionData = partnerProxy.validatePartner(
					new PartnerServiceDto(user.getPartnerCredential(),
							user.getEnrollmentDetail().getSubscriptionData()),
					user.getEnrollmentDetail().getPartnerNumber());
                LOGGER.info("Subtype validation successful for partner number -> {}",user.getEnrollmentDetail().getPartnerNumber());
			}catch(RuntimeException ex) {
			    LOGGER.error("Original exception: {}", ex.getMessage());
				ExceptionResponse errorResponse = ExceptionResponse.fromJson(ex.getMessage());
				throw new ValidationException(errorResponse.getErrorcode(),
						errorResponse.getMessage(),
						errorResponse.getStatus());
			}
			
            if (subscriptionData.getBody() != null) {
                LOGGER.info("partner number-> {} validated successfully",
                        user.getEnrollmentDetail().getPartnerNumber());

                // Subscriber Number Generated
                final String SUBSCRIBER_NUMBER = AuthService.generateRandomAlphaNumeric();
                LOGGER.info("Generated Subscriber Number {}", SUBSCRIBER_NUMBER);

                ResponseEntity<Boolean> thirdPartyResponse = thirdPartyProxy.createCustomer(
                        new ThirdPartyEntityDto(user.getEnrollmentDetail().getPartnerNumber(), SUBSCRIBER_NUMBER,
                                user.getEnrollmentDetail().getSubscriptionData().getSubtypeNumber()));
                LOGGER.info("Successful response from third party");
                if (Boolean.TRUE.equals(thirdPartyResponse.getBody())) {
                    final Subscriber ENROLLED_SUBSCRIBER = service.enrollNewSubscriber(user, SUBSCRIBER_NUMBER);
//================================================================================================
//===========================    BILLING SERVICE    (1)  ===============================================
                    // ADD in Billing Pending Table   AND   KAFKA :

                    CompletableFuture.runAsync(()->{
                        BillingPending billingPending = new BillingPending(SUBSCRIBER_NUMBER, user.getEnrollmentDetail().getPartnerNumber(),
                                subscriptionData.getBody().getSubtypeNumber(),
                                subscriptionData.getBody().getPricingRoutine(), "BILLING-PENDING"
                        );
                        LOGGER.info("Billing pending status database entry initiated for subscriber number -> {}",SUBSCRIBER_NUMBER);
                        billingService.saveBillingPendingEntry(billingPending);
                        LOGGER.info("Billing pending status database entry completed for subscriber number -> {}",SUBSCRIBER_NUMBER);
                        billingProducer.sendMessage(billingPending);
                        LOGGER.info("Billing detail send to billing service successfully for subscriber number -> {}",SUBSCRIBER_NUMBER);
                    });

//=================================   ENDED  =========================================================
//====================================================================================================

//================================================================================================
//===========================    EMAIL SERVICE     (2) ===============================================
                    CompletableFuture.runAsync(() ->{
                        LOGGER.info("Email pending status database entry initiated for subscriber number -> {}",SUBSCRIBER_NUMBER);
                        EmailPending emailPending = emailService.addPendingEntry(new EmailPending(SUBSCRIBER_NUMBER, user.getEnrollmentDetail().getSubscriberData().getEmail(), 800, "EMAIL_PENDING"));
                        LOGGER.info("Email pending status database entry completed for subscriber number -> {}",SUBSCRIBER_NUMBER);
                        rabbitMQProducer.sendMessage(emailPending);
                        LOGGER.info("Email detail send to Email service successfully for subscriber number -> {}",SUBSCRIBER_NUMBER);
                    });

//=================================   ENDED  =========================================================
//====================================================================================================

                    LOGGER.info("returning response entity");
                    return ResponseEntity.ok("success");
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
