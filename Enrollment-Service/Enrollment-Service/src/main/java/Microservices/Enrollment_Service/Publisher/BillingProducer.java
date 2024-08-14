package Microservices.Enrollment_Service.Publisher;

import Microservices.Enrollment_Service.Dto.SubscriptionBillingDto;
import Microservices.Enrollment_Service.Entity.BillingPending;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class BillingProducer {

    private KafkaTemplate<String, String> kafkaTemplate2;
    private final Logger LOGGER = LoggerFactory.getLogger(BillingProducer.class);

    public BillingProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate2 = kafkaTemplate;
    }

    public void sendMessage(BillingPending billingPending){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        try {
            // Convert POJO to JSON string
            String jsonString = objectMapper.writeValueAsString(billingPending);

            Message<String> message = MessageBuilder
                    .withPayload(jsonString)
                    .setHeader(KafkaHeaders.TOPIC,"billing")
                    .build();

            LOGGER.info("Message is ready to be produced and sent to kafka topic." +
                    " message : {}",message);

            kafkaTemplate2.send(message);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


    }

}

