package com.eventhub.event_service.application.port.out;

import com.eventhub.event_service.domain.entity.Event;

import java.util.List;


public interface EventOutputPort {
    Event save(Event event);

    void deleteAll();

   List<Event> list();
}
