package Microservices.Enrollment_Service.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient(name="TOKEN-SERVICE")
public interface JwtServerProxy {
    @GetMapping("generate/token/{partner_number}")
    public ResponseEntity<?> tokenGenerationForPartner(@PathVariable("partner_number") long partnerNumber);
    
    @GetMapping("validate/token/")
    public ResponseEntity<?> validateToken(@RequestHeader(value = "Authorization", required = false) String token);

}
