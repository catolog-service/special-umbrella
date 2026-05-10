package com.eventhub.event_service.application.service;

import com.eventhub.event_service.application.port.in.EventInputPort;
import com.eventhub.event_service.application.port.out.EventOutputPort;
import com.eventhub.event_service.application.usecase.CreateEventUseCase;
import com.eventhub.event_service.domain.entity.Event;
import org.springframework.stereotype.Service;

@Service
public class EventApplicationService  implements CreateEventUseCase, EventInputPort {

    private final EventOutputPort outputPort;

    public EventApplicationService(EventOutputPort outputPort) {
        this.outputPort = outputPort;
    }

    @Override
    public Event create(Event event) {
        return outputPort.save(event);
    }
}
