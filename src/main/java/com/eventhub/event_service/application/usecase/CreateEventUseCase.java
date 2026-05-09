package com.eventhub.event_service.application.usecase;

import com.eventhub.event_service.domain.entity.Event;

public interface CreateEventUseCase {
    Event create(Event event);
}
