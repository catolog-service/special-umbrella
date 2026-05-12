package com.eventhub.event_service.adapter.in.web;

import com.eventhub.event_service.adapter.in.web.dto.EventCreateRequest;
import com.eventhub.event_service.application.port.in.CreateEventUseCase;
import com.eventhub.event_service.application.port.in.DeleteEventUseCase;
import com.eventhub.event_service.application.port.in.ListEventUsecase;
import com.eventhub.event_service.domain.entity.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EventController - Testes Unitários")
class EventControllerTest {

    @Mock
    private CreateEventUseCase createEventUseCase;

    @Mock
    private DeleteEventUseCase deleteEventUseCase;

    @Mock
    private ListEventUsecase listEventUsecase;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UUID eventId;
    private Event testEvent;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(
                new EventController(createEventUseCase, deleteEventUseCase, listEventUsecase)
        ).build();

        eventId = UUID.randomUUID();
        testEvent = new Event(
                eventId,
                "Workshop Spring",
                "Aprenda Spring Boot",
                LocalDateTime.of(2027, 6, 15, 10, 0),
                "São Paulo"
        );
    }

    @Test
    @DisplayName("POST /api/events - Deve criar evento com sucesso")
    void testCreateEventSuccess() throws Exception {
        // Arrange
        EventCreateRequest request = new EventCreateRequest(
                "Workshop Spring",
                "Aprenda Spring Boot",
                LocalDateTime.of(2027, 6, 15, 10, 0),
                "São Paulo"
        );
        when(createEventUseCase.create(any(Event.class))).thenReturn(testEvent);

        // Act & Assert
        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(eventId.toString()))
                .andExpect(jsonPath("$.name").value("Workshop Spring"));

        verify(createEventUseCase, times(1)).create(any(Event.class));
    }

    @Test
    @DisplayName("POST /api/events - Deve rejeitar nome vazio")
    void testCreateEventInvalidName() throws Exception {
        // Arrange
        EventCreateRequest request = new EventCreateRequest();
        request.setName("");

        // Act & Assert
        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/events - Deve listar todos os eventos")
    void testListEventsSuccess() throws Exception {
        // Arrange
        when(listEventUsecase.list()).thenReturn(Arrays.asList(testEvent));

        // Act & Assert
        mockMvc.perform(get("/api/events")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Workshop Spring"))
                .andExpect(jsonPath("$[0].location").value("São Paulo"));

        verify(listEventUsecase, times(1)).list();
    }

    @Test
    @DisplayName("GET /api/events - Deve retornar lista vazia")
    void testListEventsEmpty() throws Exception {
        // Arrange
        when(listEventUsecase.list()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/events")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("DELETE /api/events - Deve deletar todos os eventos")
    void testDeleteAllSuccess() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/events")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(deleteEventUseCase, times(1)).deleteAll();
    }
}

