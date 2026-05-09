package com.eventhub.event_service.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * DTO de entrada para criar um evento.
 * Responsabilidade: Validar dados de entrada da API.
 * Localização: Adapter IN (web).
 */
@Schema(
        name = "EventCreateRequest",
        description = "Requisição para criar um novo evento"
)
public class EventCreateRequest {

    @Schema(
            description = "Nome do evento",
            example = "Conferência Spring",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;

    @Schema(
            description = "Descrição detalhada do evento",
            example = "Conferência anual de Spring Framework"
    )
    private String description;

    @Schema(
            description = "Data e hora do evento (ISO 8601)",
            example = "2026-06-15T10:00:00"
    )
    private LocalDateTime dateTime;

    @Schema(
            description = "Local onde o evento acontecerá",
            example = "São Paulo"
    )
    private String location;

    // Construtores
    public EventCreateRequest() {
    }

    public EventCreateRequest(String name, String description, LocalDateTime dateTime, String location) {
        this.name = name;
        this.description = description;
        this.dateTime = dateTime;
        this.location = location;
    }

    // Getters e Setters
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
}

