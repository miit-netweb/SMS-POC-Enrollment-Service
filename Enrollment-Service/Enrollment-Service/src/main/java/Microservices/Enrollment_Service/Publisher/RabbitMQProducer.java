
package Microservices.Enrollment_Service.Publisher;

import Microservices.Enrollment_Service.Dto.EmailMessageDto;
import Microservices.Enrollment_Service.Entity.EmailPending;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {

    @Value("${rabbitmq.exchange.name}")
    private String EXCHANGE_NAME;

    @Value("${rabbitmq.routing.key}")
    private String ROUTING_KEY;

    private RabbitTemplate rabbitTemplate;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    @Async
    public void sendMessage(EmailPending message){
        System.out.println(String.format("Message sent -> %s",message));
        try {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME,ROUTING_KEY,message);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
