package Microservices.Enrollment_Service.Controller;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Microservices.Enrollment_Service.Dto.EmailMessageDto;
import Microservices.Enrollment_Service.Dto.SubscriberDto;
import Microservices.Enrollment_Service.Publisher.RabbitMQProducer;
import Microservices.Enrollment_Service.Service.AuthService;
import Microservices.Enrollment_Service.proxy.JwtServerProxy;
import Microservices.Enrollment_Service.proxy.PartnerServiceProxy;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private AuthService service;

	@Autowired
	private JwtServerProxy jwtServerProxy;
	@Autowired
	private PartnerServiceProxy partnerProxy;
	@Autowired
	private RabbitMQProducer rabbitMQProducer;

	@PostMapping("/register")
	public ResponseEntity<?> addNewUser(@RequestBody SubscriberDto user) {
		LOGGER.info("Started validating user");
		if (service.ValidateResponse(user) == true) {
			LOGGER.info("Validation successful for user of partner number-> {}",
					user.getEnrollmentDetail().getPartnerNumber());
			//ResponseDto responseDto = service.checkPartnerNumber(user);
			ResponseEntity<Boolean> responseDto=partnerProxy.validatePartner(user.getPartnerCredential(), user.getEnrollmentDetail().getPartnerNumber());

			if (responseDto.getBody() == true) {
				LOGGER.info("partner number-> {} validated successfully",
						user.getEnrollmentDetail().getPartnerNumber());
				ResponseEntity<?> tokenGenerationResponseDto = jwtServerProxy
						.tokenGenerationForPartner(user.getEnrollmentDetail().getPartnerNumber());
				LOGGER.info("JWT Token generated successfully for user of partner number-> {}",
						user.getEnrollmentDetail().getPartnerNumber());
				EmailMessageDto emailMessageDto = new EmailMessageDto(
						user.getEnrollmentDetail().getSubscriberData().getEmail(),
						user.getEnrollmentDetail().getSubscriberData().getFirstName(),
						user.getEnrollmentDetail().getSubscriberData().getLastName(), 99995555L, 800);
				CompletableFuture.runAsync(() -> rabbitMQProducer.sendMessage(emailMessageDto));
				LOGGER.info("returning response entity");
				LOGGER.info(Thread.currentThread().getName());

				return ResponseEntity.ok(tokenGenerationResponseDto);
			} else {
				LOGGER.error("Invalid partner number-> {} for user enrollment",
						user.getEnrollmentDetail().getPartnerNumber());
				return ResponseEntity.badRequest().body(responseDto);
			}
		} else {
			LOGGER.error("Validation failed for user");
			return ResponseEntity.badRequest().body(service.ValidateResponse(user));
		}
	}

}