package com.eventhub.event_service.application.service;

import com.eventhub.event_service.application.port.in.CreateEventUseCase;
import com.eventhub.event_service.application.port.in.DeleteEventUseCase;
import com.eventhub.event_service.application.port.in.ListEventUsecase;
import com.eventhub.event_service.application.port.out.EventOutputPort;
import com.eventhub.event_service.application.port.out.EventPublisher;
import com.eventhub.event_service.domain.entity.Event;
import org.springframework.stereotype.Service;

import java.util.List;


@Service

public class EventApplicationService implements CreateEventUseCase, DeleteEventUseCase,ListEventUsecase {


    private final EventOutputPort outputPort;
    private final EventPublisher eventPublisher;

    public EventApplicationService(EventOutputPort outputPort, EventPublisher eventPublisher) {
        this.outputPort = outputPort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Event create(Event event) {
        if (event == null) {
            throw new NullPointerException("Event cannot be null");
        }
        Event created = outputPort.save(event);
        eventPublisher.publish(created);

        return created;
    }

    @Override
    public void deleteAll() {
        outputPort.deleteAll();
        eventPublisher.purgeAll();
    }


    @Override
    public List<Event>  list() {
        return outputPort.list();
    }
}


