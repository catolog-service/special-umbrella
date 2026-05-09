package com.eventhub.event_service.adapter.in.web;

import com.eventhub.event_service.adapter.in.web.dto.EventCreateRequest;
import com.eventhub.event_service.adapter.in.web.dto.EventResponse;
import com.eventhub.event_service.application.port.in.EventInputPort;
import com.eventhub.event_service.domain.entity.Event;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/events")
@Tag(name = "Events", description = "Endpoints para gerenciar eventos")
public class EventController {

    private final EventInputPort eventInputPort;

    public EventController(EventInputPort eventInputPort) {
        this.eventInputPort = eventInputPort;
    }

    @PostMapping
    @Operation(
            summary = "Criar um novo evento",
            description = "Cria um novo evento no sistema com os dados fornecidos"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Evento criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EventResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida - dados obrigatórios faltando"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor"
            )
    })
    public ResponseEntity<EventResponse> create(@RequestBody EventCreateRequest request) {
        // 1. Mapear DTO → Entidade de domínio
        Event eventDomain = new Event(
                null, // ID será gerado no domínio
                request.getName(),
                request.getDescription(),
                request.getDateTime(),
                request.getLocation()
        );

        // 2. Chamar porta de entrada (use case)
        Event created = eventInputPort.create(eventDomain);

        // 3. Mapear resultado → DTO de resposta
        EventResponse response = new EventResponse(
                created.getId(),
                created.getName(),
                created.getDescription(),
                created.getDateTime(),
                created.getLocation()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}


