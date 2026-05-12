package com.eventhub.event_service.adapter.in.web;

import com.eventhub.event_service.adapter.in.web.dto.EventCreateRequest;
import com.eventhub.event_service.adapter.in.web.dto.EventResponse;
import com.eventhub.event_service.application.port.in.CreateEventUseCase;
import com.eventhub.event_service.application.port.in.DeleteEventUseCase;
import com.eventhub.event_service.application.port.in.ListEventUsecase;
import com.eventhub.event_service.domain.entity.Event;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/events")
@Tag(name = "Events", description = "Endpoints para gerenciar eventos")
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final CreateEventUseCase createEventUseCase;
    private final DeleteEventUseCase deleteEventUseCase;
    private final ListEventUsecase listEventUsecase;


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
    public ResponseEntity<EventResponse> create(@Valid @RequestBody EventCreateRequest request) {
git        log.info("🔵 [CREATE] Iniciando criação de evento: {}", request.getName());
        log.debug("📋 [CREATE] Detalhes: descrição={}, dataHora={}, localização={}",
                request.getDescription(), request.getDateTime(), request.getLocation());

        try {
            // 1. Mapear DTO → Entidade de domínio
            Event eventDomain = new Event(
                    null, // ID será gerado no domínio
                    request.getName(),
                    request.getDescription(),
                    request.getDateTime(),
                    request.getLocation()
            );
            log.debug("✅ [CREATE] Entidade de domínio criada com ID gerado: {}", eventDomain.getId());

            // 2. Chamar porta de entrada (use case)
            Event created = createEventUseCase.create(eventDomain);
            log.info("✨ [CREATE] Evento criado com sucesso! ID: {}", created.getId());

            // 3. Mapear resultado → DTO de resposta
            EventResponse response = new EventResponse(
                    created.getId(),
                    created.getName(),
                    created.getDescription(),
                    created.getDateTime(),
                    created.getLocation()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("❌ [CREATE] Erro ao criar evento: {}", request.getName(), e);
            throw e;
        }
    }

    @DeleteMapping
    @Operation(summary = "Deletar todos os eventos")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Todos os eventos foram deletados com sucesso"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado"
            )
    })
    public ResponseEntity<Void> deleteAll() {
        log.warn("🟡 [DELETE] Iniciando deleção de TODOS os eventos");
        try {
            deleteEventUseCase.deleteAll();
            log.info("✨ [DELETE] Todos os eventos foram deletados com sucesso");
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("❌ [DELETE] Erro ao deletar todos os eventos", e);
            throw e;
        }
    }

    @GetMapping
    @Operation(
            summary = "Listar todos os eventos",
            description = "Retorna todos os eventos cadastrados no sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de eventos retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EventResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor"
            )
    })

    public ResponseEntity<List<EventResponse>> list() {
        log.info("🔵 [LIST] Iniciando listagem de todos os eventos");
        try {
            List<Event> events = listEventUsecase.list();
            log.info("📊 [LIST] Total de eventos encontrados: {}", events.size());

            List<EventResponse> response = events.stream()
                    .map(event -> new EventResponse(
                            event.getId(),
                            event.getName(),
                            event.getDescription(),
                            event.getDateTime(),
                            event.getLocation()
                    ))
                    .toList();

            log.debug("✅ [LIST] Resposta mapeada com {} eventos", response.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("❌ [LIST] Erro ao listar eventos", e);
            throw e;
        }
    }
}


