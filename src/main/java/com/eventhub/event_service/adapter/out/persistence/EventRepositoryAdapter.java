package com.eventhub.event_service.adapter.out.persistence;

import com.eventhub.event_service.application.port.out.EventOutputPort;
import com.eventhub.event_service.domain.entity.Event;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class EventRepositoryAdapter implements EventOutputPort {

    private final EventJpaRepository jpaRepository;
    private final EntityManager entityManager;

    public EventRepositoryAdapter(EventJpaRepository jpaRepository, EntityManager entityManager) {
        this.jpaRepository = jpaRepository;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Event save(Event event) {
        // Mapear Event (domínio) → EventEntity (JPA)
        EventEntity entity = new EventEntity();
        entity.setName(event.getName());
        entity.setDescription(event.getDescription());
        entity.setDateTime(event.getDateTime());
        entity.setLocation(event.getLocation());

        // Hibernatate gera o UUID via @GeneratedValue
        entityManager.persist(entity);
        entityManager.flush();

        // Mapear EventEntity → Event (domínio)
        return new Event(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getDateTime(),
                entity.getLocation()
        );
    }

    @Override
    public Optional<Event> findById(UUID id) {
        throw new UnsupportedOperationException("Método não implementado ainda");
    }

    @Override
    public List<Event> findAll() {
        throw new UnsupportedOperationException("Método não implementado ainda");
    }

    @Override
    public void deleteById(UUID id) {
        throw new UnsupportedOperationException("Método não implementado ainda");
    }
}

