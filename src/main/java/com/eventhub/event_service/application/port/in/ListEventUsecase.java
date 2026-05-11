package com.eventhub.event_service.application.port.in;

import com.eventhub.event_service.domain.entity.Event;

import java.util.List;

public interface ListEventUsecase {
    List<Event> list();
}
