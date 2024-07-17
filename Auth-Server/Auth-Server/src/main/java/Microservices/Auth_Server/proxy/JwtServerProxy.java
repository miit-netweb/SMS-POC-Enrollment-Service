package Microservices.Auth_Server.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name="JWT-Server", url = "http://localhost:8765/generate")
public interface JwtServerProxy {
    @GetMapping("/token/{partner_number}")
    public ResponseEntity<?> tokenGenerationForPartner(@PathVariable("partner_number") long partnerNumber);
}
