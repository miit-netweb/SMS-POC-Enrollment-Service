package Microservices.Enrollment_Service.Publisher;

import Microservices.Enrollment_Service.Dto.EnrollmentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentStatusProducer {

    private KafkaTemplate<String, EnrollmentStatus> kafkaTemplate;
    private final Logger LOGGER = LoggerFactory.getLogger(EnrollmentStatus.class);

    public EnrollmentStatusProducer(KafkaTemplate<String, EnrollmentStatus> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(EnrollmentStatus enrollmentStatus){
        kafkaTemplate.send("enrollment-status",enrollmentStatus);
    }

}

