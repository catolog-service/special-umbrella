package com.eventhub.event_service.application.port.in;

import com.eventhub.event_service.application.service.EventApplicationService;
import com.eventhub.event_service.application.port.out.EventOutputPort;
import com.eventhub.event_service.application.port.out.EventPublisher;
import com.eventhub.event_service.domain.entity.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ListEventUsecase - Testes Unitários")
class ListEventUsecaseTest {

    @Mock
    private EventOutputPort eventOutputPort;

    @Mock
    private EventPublisher eventPublisher;

    private ListEventUsecase useCase;

    @BeforeEach
    void setUp() {
        useCase = new EventApplicationService(eventOutputPort, eventPublisher);
    }

    @Test
    @DisplayName("Deve listar todos os eventos")
    void testListEventsSuccess() {
        // Arrange
        List<Event> events = Arrays.asList(
                new Event(UUID.randomUUID(), "Evento 1", null, null, null),
                new Event(UUID.randomUUID(), "Evento 2", null, null, null)
        );
        when(eventOutputPort.list()).thenReturn(events);

        // Act
        List<Event> result = useCase.list();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(events);
    }

    @Test
    @DisplayName("Deve retornar lista vazia")
    void testListEventsEmpty() {
        // Arrange
        when(eventOutputPort.list()).thenReturn(Collections.emptyList());

        // Act
        List<Event> result = useCase.list();

        // Assert
        assertThat(result).isEmpty();
    }
}

