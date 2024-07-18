package Microservices.Auth_Server.Service;

import Microservices.Auth_Server.Dto.TokenGenerationResponseDto;
import Microservices.Auth_Server.Entity.PartnerTokenValidation;
import Microservices.Auth_Server.Repository.PartnerTokenValidationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PartnerTokenValidationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PartnerTokenValidationService.class);
    @Autowired
    private PartnerTokenValidationRepository partnerTokenValidationRepository;

    @Autowired
    private JwtService jwtService;

    @Value("${generate.token.expiry}")
    private int expiryMinutes;

    public PartnerTokenValidation isPartnerExpired(long partnerNumber){
        Optional<PartnerTokenValidation> partner = partnerTokenValidationRepository.findById(partnerNumber);
        if(partner.isEmpty()) return null;
        final LocalDateTime expiry = partner.get().getExpiry();
        if(expiry.isBefore(LocalDateTime.now())) return null;
        return partner.get();
    }

    public PartnerTokenValidation addNewPartner(PartnerTokenValidation partner) throws Exception{
        try{
            final PartnerTokenValidation save = partnerTokenValidationRepository.save(partner);
            return save;
        } catch (Exception e){
            throw new Exception("Unable to create partner-token in DB");
        }
    }

    public TokenGenerationResponseDto generatePartnerNumberToken(long partnerNumber){
        LOGGER.info(String.format("Started creating token for partner number %s",partnerNumber));
        PartnerTokenValidation checkedPartnerToken = isPartnerExpired(partnerNumber);
        if(checkedPartnerToken!=null){
            TokenGenerationResponseDto tokenGenerationResponseDto = new TokenGenerationResponseDto();
            tokenGenerationResponseDto.setToken(checkedPartnerToken.getToken());
            tokenGenerationResponseDto.setExpiry(checkedPartnerToken.getExpiry());
            LOGGER.info(String.format("Saved token return backed for %s", partnerNumber));
            return tokenGenerationResponseDto;
        } else {
            String token = jwtService.generateToken(String.valueOf(partnerNumber));
            if (!token.isEmpty()) {
                LOGGER.info(String.format("Token saved for %s", partnerNumber));
                try {
                    addNewPartner(new PartnerTokenValidation(partnerNumber,LocalDateTime.now().plusMinutes(expiryMinutes),token));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                TokenGenerationResponseDto tokenGenerationResponseDto = new TokenGenerationResponseDto();
                tokenGenerationResponseDto.setToken(token);
                tokenGenerationResponseDto.setExpiry(LocalDateTime.now().plusMinutes(expiryMinutes));
                LOGGER.info(String.format("Token created for %s", partnerNumber));
                return tokenGenerationResponseDto;
            } else {
                LOGGER.error(String.format("Unable to make token for partner Number %s", partnerNumber));
            }
            return null;
        }
    }
}
