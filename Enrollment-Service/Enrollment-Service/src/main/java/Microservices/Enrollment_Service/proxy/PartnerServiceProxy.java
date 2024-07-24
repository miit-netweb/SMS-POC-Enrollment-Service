package Microservices.Enrollment_Service.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import Microservices.Enrollment_Service.Dto.PartnerCredential;

@FeignClient(name="PARTNER-SERVICE")
public interface PartnerServiceProxy {

	@PostMapping("partner/validate/{partnerNumber}")
	public ResponseEntity<Boolean> validatePartner(@RequestBody PartnerCredential credential,@PathVariable Long partnerNumber);
	
}
