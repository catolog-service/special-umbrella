package com.eventhub.event_service.application.service;

import com.eventhub.event_service.application.port.in.CreateEventUseCase;
import com.eventhub.event_service.application.port.in.DeleteEventUseCase;
import com.eventhub.event_service.application.port.in.ListEventUsecase;
import com.eventhub.event_service.application.port.out.EventOutputPort;
import com.eventhub.event_service.domain.entity.Event;
import org.springframework.stereotype.Service;

import java.util.List;


@Service

public class EventApplicationService implements CreateEventUseCase, DeleteEventUseCase,ListEventUsecase {


    private final EventOutputPort outputPort;

    public EventApplicationService(EventOutputPort outputPort) {
        this.outputPort = outputPort;
    }

    @Override
    public Event create(Event event) {

        Event created = outputPort.save(event);

        return created;
    }

    @Override
    public void deleteAll() {
        outputPort.deleteAll();
    }


    @Override
    public List<Event>  list() {
        return outputPort.list();
    }
}


