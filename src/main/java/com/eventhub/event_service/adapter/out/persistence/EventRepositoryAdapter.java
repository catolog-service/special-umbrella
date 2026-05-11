package com.eventhub.event_service.adapter.out.persistence;

import com.eventhub.event_service.application.port.out.EventOutputPort;
import com.eventhub.event_service.domain.entity.Event;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Component
public class EventRepositoryAdapter implements EventOutputPort {

    private final EventJpaRepository jpaRepository;

    public EventRepositoryAdapter(EventJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional
    public Event save(Event event) {
        // Domínio → Entidade JPA (ID já vem do domínio)
        EventEntity entity = new EventEntity();
        entity.setId(event.getId());
        entity.setName(event.getName());
        entity.setDescription(event.getDescription());
        entity.setDateTime(event.getDateTime());
        entity.setLocation(event.getLocation());

        EventEntity saved = jpaRepository.save(entity);

        // Entidade JPA → Domínio
        return new Event(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.getDateTime(),
                saved.getLocation()
        );
    }

    @Transactional
    public void deleteAll() {
        jpaRepository.deleteAll();

    }


    @Override
    public List<Event> list() {
        return jpaRepository.findAll().stream()
                .map(saved -> new Event(
                        saved.getId(),
                        saved.getName(),
                        saved.getDescription(),
                        saved.getDateTime(),
                        saved.getLocation()
                ))
                .toList();


    }
}



