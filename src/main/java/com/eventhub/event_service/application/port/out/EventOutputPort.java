package com.eventhub.event_service.application.port.out;

import com.eventhub.event_service.domain.entity.Event;


public interface EventOutputPort {
    Event save(Event event);

}
