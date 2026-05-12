package com.eventhub.event_service.application.port.out;

import com.eventhub.event_service.domain.entity.Event;

public interface EventPublisher {
    void publish(Event event);
    void purgeAll();
}
