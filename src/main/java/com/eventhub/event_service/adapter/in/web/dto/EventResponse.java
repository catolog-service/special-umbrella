package com.eventhub.event_service.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO de resposta ao cliente.
 * Responsabilidade: Serializar Event para JSON.
 * Localização: Adapter IN (web).
 */
@Schema(
        name = "EventResponse",
        description = "Resposta contendo os dados do evento"
)
public class EventResponse {

    @Schema(
            description = "ID único do evento (gerado automaticamente)",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    private UUID id;

    @Schema(
            description = "Nome do evento",
            example = "Conferência Spring"
    )
    private String name;

    @Schema(
            description = "Descrição detalhada do evento",
            example = "Conferência anual de Spring Framework"
    )
    private String description;

    @Schema(
            description = "Data e hora do evento",
            example = "2026-06-15T10:00:00"
    )
    private LocalDateTime dateTime;

    @Schema(
            description = "Local onde o evento acontecerá",
            example = "São Paulo"
    )
    private String location;

    // Construtores
    public EventResponse() {
    }

    public EventResponse(UUID id, String name, String description, LocalDateTime dateTime, String location) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dateTime = dateTime;
        this.location = location;
    }

    // Getters e Setters
    public UUID getId() {
        return id;
    }

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
}

