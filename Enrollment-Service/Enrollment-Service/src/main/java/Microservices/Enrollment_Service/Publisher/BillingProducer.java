package Microservices.Enrollment_Service.Publisher;

import Microservices.Enrollment_Service.Dto.SubscriptionBillingDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class BillingProducer {


    private KafkaTemplate<String, String> kafkaTemplate;

    public BillingProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(SubscriptionBillingDto subscriptionBillingDto){

        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.findAndRegisterModules();

        try {
            // Convert POJO to JSON string
            String jsonString = objectMapper.writeValueAsString(subscriptionBillingDto);

            System.out.println( " STRING : "+ jsonString);

            Message<String> message = MessageBuilder
                    .withPayload(jsonString)
                    .setHeader(KafkaHeaders.TOPIC,"billing")
                    .build();

            kafkaTemplate.send(message);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


    }

}

