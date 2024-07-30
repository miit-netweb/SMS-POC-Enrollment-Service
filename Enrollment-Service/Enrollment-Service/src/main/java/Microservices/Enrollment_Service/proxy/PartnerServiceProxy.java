package Microservices.Enrollment_Service.proxy;

import Microservices.Enrollment_Service.Dto.SubscriptionData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import Microservices.Enrollment_Service.Dto.PartnerServiceDto;

@FeignClient(name="PARTNER-SERVICE")
public interface PartnerServiceProxy {

	@PostMapping("partner/validate/{partnerNumber}")
	public ResponseEntity<SubscriptionData> validatePartner(@RequestBody PartnerServiceDto serviceDto, @PathVariable Long partnerNumber);
	
}
