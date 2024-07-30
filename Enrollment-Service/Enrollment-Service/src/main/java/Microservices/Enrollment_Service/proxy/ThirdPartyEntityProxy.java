package Microservices.Enrollment_Service.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import Microservices.Enrollment_Service.Dto.ThirdPartyEntityDto;

@FeignClient(name="THIRDPARTY-ENTITY")
public interface ThirdPartyEntityProxy {
	
	@PostMapping("/create/customer")
	public ResponseEntity<Boolean> createCustomer(@RequestBody ThirdPartyEntityDto thirdPartyEntityDto); 

}
