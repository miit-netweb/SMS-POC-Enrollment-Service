package Microservices.Enrollment_Service.Controller;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import Microservices.Enrollment_Service.Dto.EnrollmentStatus;
import Microservices.Enrollment_Service.Publisher.EnrollmentStatusProducer;

@RestController
public class dummyController {

    @Autowired
    private EnrollmentStatusProducer enrollProducer;

    @GetMapping("/temp")
    public String tempmethod(){
        EnrollmentStatus enrollStatus = new EnrollmentStatus(LocalDateTime.now().toString(), "ENROLLMENT-SUCCESS");
        Random random = new Random();
        int min = 1;
        int max = 10;

        int randomNumber = random.nextInt(max - min + 1) + min;
        if(randomNumber<4){
        	enrollProducer.sendMessage(new EnrollmentStatus(LocalDateTime.now().toString(),"ENROLLMENT-FAILURE"));
        } else {
        	enrollProducer.sendMessage(enrollStatus);
        }
        return "sent";
    }

}
