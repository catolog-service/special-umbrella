package com.eventhub.event_service.adapter.out.messaging;

import com.eventhub.event_service.application.port.out.EventPublisher;
import com.eventhub.event_service.config.RabbitMqConfig;
import com.eventhub.event_service.domain.entity.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class EventPublisherAdapter implements EventPublisher
{
    public static final String EXCHANGE_NAME = "event-service-exchange";
    public static final String ROUTING_KEY = "event.created";
    private final RabbitAdmin rabbitAdmin;


    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public EventPublisherAdapter(RabbitAdmin rabbitAdmin, RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitAdmin = rabbitAdmin;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publish(Event event) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("id", event.getId().toString());
            payload.put("name", event.getName());
            payload.put("description", event.getDescription());
            payload.put("date", event.getDateTime() != null ? event.getDateTime().toString() : null);
            payload.put("location", event.getLocation());
            payload.put("timestamp", System.currentTimeMillis());

            String json = objectMapper.writeValueAsString(payload);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, json);
            log.info("Evento publicado no RabbitMQ: {}", event.getId());

        } catch (Exception e) {
            log.error("Falha ao publicar evento", e);
            throw new RuntimeException("Falha ao publicar evento", e);
        }
    }

    @Override
    public void purgeAll() {
        try {
            rabbitAdmin.purgeQueue(RabbitMqConfig.QUEUE_NAME, false);
            log.info("Fila {} expurgada com sucesso", RabbitMqConfig.QUEUE_NAME);
        } catch (Exception e) {
            log.error("Falha ao expurgar a fila {}", RabbitMqConfig.QUEUE_NAME, e);
            throw new RuntimeException("Falha ao expurgar a fila", e);
        }
    }
}

