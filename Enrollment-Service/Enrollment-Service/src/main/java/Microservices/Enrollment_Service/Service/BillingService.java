package Microservices.Enrollment_Service.Service;

import Microservices.Enrollment_Service.Entity.BillingPending;
import Microservices.Enrollment_Service.Repository.BillingPendingRepository;
import org.aspectj.apache.bcel.generic.LOOKUPSWITCH;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BillingService {


    private final Logger LOGGER = LoggerFactory.getLogger(BillingService.class);

    @Autowired
    private BillingPendingRepository billingPendingRepository;

    public BillingPending saveBillingPendingEntry(BillingPending billingPending){
        LOGGER.info("Inserting entry into billing-pending-table for {}",billingPending.getSubscriberNumber());
        return billingPendingRepository.save(billingPending);
    }


}
