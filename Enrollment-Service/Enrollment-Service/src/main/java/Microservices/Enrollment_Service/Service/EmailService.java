package Microservices.Enrollment_Service.Service;

import Microservices.Enrollment_Service.Entity.EmailPending;
import Microservices.Enrollment_Service.Repository.EmailPendingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private EmailPendingRepository emailPendingRepository;


    public EmailPending addPendingEntry(EmailPending emailPending){
        return emailPendingRepository.save(emailPending);
    }



}
