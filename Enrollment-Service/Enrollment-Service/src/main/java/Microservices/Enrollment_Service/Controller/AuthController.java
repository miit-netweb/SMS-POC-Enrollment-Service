package Microservices.Enrollment_Service.Controller;

import Microservices.Enrollment_Service.Dto.*;
import Microservices.Enrollment_Service.Service.AuthService;
import Microservices.Enrollment_Service.proxy.JwtServerProxy;
import Microservices.Enrollment_Service.publisher.RabbitMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService service;

    @Autowired
    private JwtServerProxy jwtServerProxy;
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
                   ResponseEntity<?> tokenGenerationResponseDto = jwtServerProxy.tokenGenerationForPartner(user.getEnrollmentDetail().getPartnerNumber());
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