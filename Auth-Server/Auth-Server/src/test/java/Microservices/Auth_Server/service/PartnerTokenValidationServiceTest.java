package Microservices.Auth_Server.service;

import Microservices.Auth_Server.Entity.PartnerTokenValidation;
import Microservices.Auth_Server.Repository.PartnerTokenValidationRepository;
import Microservices.Auth_Server.Service.PartnerTokenValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class PartnerTokenValidationServiceTest {
    @Mock
    PartnerTokenValidationRepository repository;
    @InjectMocks
    private PartnerTokenValidationService service;

    @Test
    void testEmptyPartnerNumber(){
        when(repository.findById(0L)).thenReturn(Optional.empty());
        PartnerTokenValidation partnerTokenValidation = service.isPartnerExpired(0L);
        assertEquals(null,partnerTokenValidation);
    }

    @Test
    void testExpiredPartnerNumber(){
        PartnerTokenValidation partnerTokenValidation = new PartnerTokenValidation();
        partnerTokenValidation.setExpiry(LocalDateTime.now().minusHours(1));
        Optional<PartnerTokenValidation> optionalPartnerTokenValidation = Optional.of(partnerTokenValidation);
        when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(optionalPartnerTokenValidation);
        PartnerTokenValidation responsePartnerToken =service.isPartnerExpired(0L);
        assertEquals(null,responsePartnerToken);
    }

    @Test
    void testValidPartnerNumber(){
        PartnerTokenValidation partnerTokenValidation = new PartnerTokenValidation();
        partnerTokenValidation.setExpiry(LocalDateTime.now().plusMinutes(2));
        Optional<PartnerTokenValidation> optionalPartnerTokenValidation = Optional.of(partnerTokenValidation);
        when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(optionalPartnerTokenValidation);
        PartnerTokenValidation responsePartnerToken =service.isPartnerExpired(0L);
        assertNotNull(responsePartnerToken);
    }


}
