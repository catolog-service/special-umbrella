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

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateEventUseCase - Testes Unitários")
class CreateEventUseCaseTest {

    @Mock
    private EventOutputPort eventOutputPort;

    @Mock
    private EventPublisher eventPublisher;

    private CreateEventUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new EventApplicationService(eventOutputPort, eventPublisher);
    }

    @Test
    @DisplayName("Deve criar evento com sucesso")
    void testCreateEventSuccess() {
        // Arrange
        Event event = new Event(
                null,
                "Workshop Spring",
                "Aprenda Spring Boot",
                LocalDateTime.of(2027, 6, 15, 10, 0),
                "São Paulo"
        );
        Event savedEvent = new Event(
                UUID.randomUUID(),
                event.getName(),
                event.getDescription(),
                event.getDateTime(),
                event.getLocation()
        );
        when(eventOutputPort.save(any(Event.class))).thenReturn(savedEvent);

        // Act
        Event result = useCase.create(event);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("Workshop Spring");
        verify(eventOutputPort, times(1)).save(any(Event.class));
    }

    @Test
    @DisplayName("Deve retornar evento salvo com ID")
    void testCreateEventReturnsWithId() {
        // Arrange
        Event event = new Event(null, "Evento", null, null, null);
        UUID eventId = UUID.randomUUID();
        Event savedEvent = new Event(eventId, "Evento", null, null, null);
        when(eventOutputPort.save(any())).thenReturn(savedEvent);

        // Act
        Event result = useCase.create(event);

        // Assert
        assertThat(result.getId()).isEqualTo(eventId);
    }
}

