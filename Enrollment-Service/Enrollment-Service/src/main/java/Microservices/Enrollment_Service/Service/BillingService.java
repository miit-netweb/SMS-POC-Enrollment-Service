package Microservices.Enrollment_Service.Service;

import Microservices.Enrollment_Service.Entity.BillingPending;
import Microservices.Enrollment_Service.Repository.BillingPendingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BillingService {


    @Autowired
    private BillingPendingRepository billingPendingRepository;

    public BillingPending saveBillingPendingEntry(BillingPending billingPending){
        return billingPendingRepository.save(billingPending);
    }


}
