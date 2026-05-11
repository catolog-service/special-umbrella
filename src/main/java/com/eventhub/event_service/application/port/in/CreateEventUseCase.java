package com.eventhub.event_service.application.port.in;

import com.eventhub.event_service.domain.entity.Event;


public interface CreateEventUseCase {
    Event create(Event event);
}

