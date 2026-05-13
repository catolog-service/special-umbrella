package com.eventhub.event_service.config;

import com.eventhub.event_service.adapter.out.messaging.EventPublisherAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfig {

    public static final String QUEUE_NAME = "event-created-queue";

    @Bean
    public TopicExchange eventExchange() {
        return new TopicExchange(EventPublisherAdapter.EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue eventCreatedQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Binding bindingEventCreated(Queue eventCreatedQueue, TopicExchange eventExchange) {
        return BindingBuilder.bind(eventCreatedQueue)
                .to(eventExchange)
                .with(EventPublisherAdapter.ROUTING_KEY);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}
