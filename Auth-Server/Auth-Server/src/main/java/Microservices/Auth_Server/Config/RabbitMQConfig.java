package Microservices.Auth_Server.Config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.name}")
    private String QUEUE_NAME;

    @Value("${rabbitmq.exchange.name}")
    private String EXCHANGE_NAME;

    @Value("${rabbitmq.routing.key}")
    private String ROUTING_KEY;

    @Bean
    public Queue queue(){
        return new Queue(QUEUE_NAME);
    }
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(exchange()).with(ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
