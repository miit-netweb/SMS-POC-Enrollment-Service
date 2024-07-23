package Microservices.Auth_Server.Controller;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Microservices.Auth_Server.Dto.EmailMessageDto;
import Microservices.Auth_Server.Dto.ResponseDto;
import Microservices.Auth_Server.Dto.SubscriberDto;
import Microservices.Auth_Server.Dto.TokenGenerationResponseDto;
import Microservices.Auth_Server.Publisher.RabbitMQProducer;
import Microservices.Auth_Server.Service.AuthService;
import Microservices.Auth_Server.Service.PartnerTokenValidationService;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private AuthService service;
	@Autowired
	private PartnerTokenValidationService partnerTokenValidationService;
	@Autowired
	private RabbitMQProducer rabbitMQProducer;

	@PostMapping("/register")
	@Retry(name="register")
	public ResponseEntity<?> addNewUser(@RequestBody SubscriberDto user) {
		LOGGER.info("Started validating user");
		if (service.ValidateResponse(user) == null) {
			LOGGER.info("Validation successful for user of partner number-> {}",
					user.getEnrollmentDetail().getPartnerNumber());
			ResponseDto responseDto = service.checkPartnerNumber(user);
			if (responseDto == null) {
				LOGGER.info("partner number-> {} validated successfully",
						user.getEnrollmentDetail().getPartnerNumber());
				TokenGenerationResponseDto tokenGenerationResponseDto = partnerTokenValidationService
						.generatePartnerNumberToken(user.getEnrollmentDetail().getPartnerNumber());
				LOGGER.info("JWT Token generated successful for user of partner number-> {}",
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