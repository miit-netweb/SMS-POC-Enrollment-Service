package Microservices.Auth_Server.Controller;

import Microservices.Auth_Server.Dto.*;
import Microservices.Auth_Server.Entity.PartnerTokenValidation;
import Microservices.Auth_Server.Service.AuthService;
import Microservices.Auth_Server.Service.JwtService;
import Microservices.Auth_Server.Service.PartnerTokenValidationService;
import Microservices.Auth_Server.proxy.JwtServerProxy;
import Microservices.Auth_Server.publisher.RabbitMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService service;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtServerProxy jwtServerProxy;
    @Autowired
    private PartnerTokenValidationService partnerTokenValidationService;
    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    
    @PostMapping("/register")
    public ResponseEntity<?> addNewUser(@RequestBody SubscriberDto user) {
        try{
            LOGGER.info(String.format("Started validating user"));
            if(service.ValidateResponse(user) == null){
                LOGGER.info(String.format("Validation successful for user of partner number-> %s",user.getEnrollmentDetail().getPartnerNumber()));
                ResponseDto responseDto = service.checkPartnerNumber(user);
               if(responseDto == null){
                   LOGGER.info(String.format("partner number-> %s validated successfully",user.getEnrollmentDetail().getPartnerNumber()));
                   TokenGenerationResponseDto tokenGenerationResponseDto = partnerTokenValidationService.generatePartnerNumberToken(user.getEnrollmentDetail().getPartnerNumber());
                   LOGGER.info(String.format("JWT Token generated successful for user of partner number-> %s",user.getEnrollmentDetail().getPartnerNumber()));
                   EmailMessageDto emailMessageDto = new EmailMessageDto(user.getEnrollmentDetail().getSubscriberData().getEmail(),user.getEnrollmentDetail().getSubscriberData().getFirstName(),user.getEnrollmentDetail().getSubscriberData().getLastName(),99995555L,800);
                   CompletableFuture.runAsync(() -> rabbitMQProducer.sendMessage(emailMessageDto));
                   System.out.println("returning response entity");
                   System.out.println(Thread.currentThread().getName());

                   return ResponseEntity.ok(tokenGenerationResponseDto);
               } else {
                   LOGGER.error(String.format("Invalid partner number-> %s for user enrollment",user.getEnrollmentDetail().getPartnerNumber()));
                   return ResponseEntity.badRequest().body(responseDto);
               }
            } else{
                LOGGER.error(String.format("Validation failed for user"));
                return ResponseEntity.badRequest().body(service.ValidateResponse(user));
            }
        } catch (Exception e){
            LOGGER.error(String.format("error-> %s occurred",e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}