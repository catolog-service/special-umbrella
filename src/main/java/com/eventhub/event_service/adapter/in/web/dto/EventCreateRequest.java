package com.eventhub.event_service.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

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

    @NotBlank(message = "O nome do evento é obrigatório e não pode estar em branco")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    @Pattern(
            regexp = "^[\\p{L}0-9\\s\\-_.,!?()]+$",
            message = "O nome contém caracteres inválidos. Apenas letras, números e pontuação básica são permitidos"
    )
    @Schema(
            description = "Nome do evento",
            example = "Conferência Spring Boot",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 3,
            maxLength = 100
    )
    private String name;

    @Size(max = 500, message = "A descrição não pode ter mais de 500 caracteres")
    @Schema(
            description = "Descrição detalhada do evento",
            example = "Conferência anual de Spring Framework",
            maxLength = 500
    )
    private String description;

    @Future(message = "A data do evento deve ser no futuro")
    @Schema(
            description = "Data e hora do evento (ISO 8601). Deve ser uma data futura.",
            example = "2027-06-15T10:00:00"
    )
    private LocalDateTime dateTime;

    @Size(max = 200, message = "A localização não pode ter mais de 200 caracteres")
    @Schema(
            description = "Local onde o evento acontecerá",
            example = "São Paulo - SP",
            maxLength = 200
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

