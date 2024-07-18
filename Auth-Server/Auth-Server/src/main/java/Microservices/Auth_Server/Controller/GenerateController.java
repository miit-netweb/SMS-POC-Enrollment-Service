package Microservices.Auth_Server.Controller;


import Microservices.Auth_Server.Dto.TokenGenerationResponseDto;
import Microservices.Auth_Server.Entity.PartnerTokenValidation;
import Microservices.Auth_Server.Service.JwtService;
import Microservices.Auth_Server.Service.PartnerTokenValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/generate")
public class GenerateController {

    @Value("${generate.token.expiry}")
    private int expiryMinutes;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private PartnerTokenValidationService partnerTokenValidationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateController.class);

    @GetMapping("/token/{partner_number}")
    public ResponseEntity<?> tokenGenerationForPartner(@PathVariable("partner_number") long partnerNumber){
        try{
            LOGGER.info(String.format("Started creating token for partner number %s",partnerNumber));
            PartnerTokenValidation checkedPartnerToken = partnerTokenValidationService.isPartnerExpired(partnerNumber);
            if(checkedPartnerToken!=null){
                TokenGenerationResponseDto tokenGenerationResponseDto = new TokenGenerationResponseDto();
                tokenGenerationResponseDto.setToken(checkedPartnerToken.getToken());
                tokenGenerationResponseDto.setExpiry(checkedPartnerToken.getExpiry());
                LOGGER.info(String.format("Saved token return backed for %s", partnerNumber));
                return ResponseEntity.ok(tokenGenerationResponseDto);
            } else {
                String token = jwtService.generateToken(String.valueOf(partnerNumber));
                System.out.println(token);
                if (!token.isEmpty()) {
                    LOGGER.info(String.format("Token saved for %s", partnerNumber));
                    partnerTokenValidationService.addNewPartner(new PartnerTokenValidation(partnerNumber,LocalDateTime.now().plusMinutes(expiryMinutes),token));
                    TokenGenerationResponseDto tokenGenerationResponseDto = new TokenGenerationResponseDto();
                    tokenGenerationResponseDto.setToken(token);
                    tokenGenerationResponseDto.setExpiry(LocalDateTime.now().plusMinutes(expiryMinutes));
                    LOGGER.info(String.format("Token created for %s", partnerNumber));
                    return ResponseEntity.ok(tokenGenerationResponseDto);
                } else {
                    LOGGER.error(String.format("Unable to make token for partner Number %s", partnerNumber));
                }
            }
        }
        catch (Exception e){
            LOGGER.error(String.format("Exception occurred %s for partner number %s",e.getMessage(),partnerNumber));
        }
        return ResponseEntity.status(500).build();
    }
}
