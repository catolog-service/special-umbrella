package com.eventhub.event_service.config;

import com.eventhub.event_service.adapter.out.messaging.EventPublisherAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@Slf4j
public class RabbitMqConfig {

    public static final String QUEUE_NAME = "event-created-queue";

    private RabbitAdmin rabbitAdmin;
    private TopicExchange topicExchange;
    private Queue queue;
    private Binding binding;

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        this.rabbitAdmin = new RabbitAdmin(connectionFactory);
        return this.rabbitAdmin;
    }

    @Bean
    public TopicExchange eventExchange() {
        this.topicExchange = new TopicExchange(EventPublisherAdapter.EXCHANGE_NAME, true, false);
        return this.topicExchange;
    }

    @Bean
    public Queue eventCreatedQueue() {
        this.queue = new Queue(QUEUE_NAME, true);
        return this.queue;
    }

    @Bean
    public Binding bindingEventCreated(Queue eventCreatedQueue, TopicExchange eventExchange) {
        this.binding = BindingBuilder.bind(eventCreatedQueue)
                .to(eventExchange)
                .with(EventPublisherAdapter.ROUTING_KEY);
        return this.binding;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeRabbitMq(ApplicationReadyEvent event) {
        try {
            log.info("🔵 [RabbitMQ] Inicializando estrutura AMQP...");

            // Declarar exchange
            rabbitAdmin.declareExchange(topicExchange);
            log.info("✅ [RabbitMQ] Exchange '{}' criado/verificado", topicExchange.getName());

            // Declarar fila
            rabbitAdmin.declareQueue(queue);
            log.info("✅ [RabbitMQ] Fila '{}' criada/verificada", queue.getName());

            // Declarar binding
            rabbitAdmin.declareBinding(binding);
            log.info("✅ [RabbitMQ] Binding criado/verificado entre '{}' e '{}' com routing key '{}'",
                     topicExchange.getName(), queue.getName(), EventPublisherAdapter.ROUTING_KEY);

            log.info("✨ [RabbitMQ] Infraestrutura AMQP inicializada com sucesso!");
        } catch (Exception e) {
            log.error("❌ [RabbitMQ] Erro ao inicializar infraestrutura AMQP", e);
            throw new RuntimeException("Falha ao inicializar RabbitMQ", e);
        }
    }
}
