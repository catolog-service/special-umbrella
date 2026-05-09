package com.eventhub.event_service.application.port.out;

import com.eventhub.event_service.domain.entity.Event;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventOutputPort {
    Event save(Event event);
    Optional<Event> findById(UUID id);
    List<Event> findAll();
    void deleteById(UUID id);

}
