package Microservices.Auth_Server.publisher;

import Microservices.Auth_Server.Dto.EmailMessageDto;
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
    public void sendMessage(EmailMessageDto message){
        System.out.println(String.format("Message sent -> %s",message));
        System.out.println(Thread.currentThread().getName());
        try {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME,ROUTING_KEY,message);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
