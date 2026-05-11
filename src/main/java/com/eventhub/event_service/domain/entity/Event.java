package com.eventhub.event_service.domain.entity;

import java.sql.ConnectionBuilder;
import java.time.LocalDateTime;
import java.util.UUID;


public class Event {
    private UUID id;
    private String name;
    private String description;
    private LocalDateTime dateTime;
    private String location;


    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Event() {
        this.id = UUID.randomUUID();
    }

    public Event(UUID id, String name, String description, LocalDateTime dateTime, String location) {
        this.id = (id != null) ? id : UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.dateTime = dateTime;
        this.location = location;
    }

    public UUID getId() {
        return id;
    }
}
