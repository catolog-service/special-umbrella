package com.eventhub.event_service.application.port.in;

import com.eventhub.event_service.application.service.EventApplicationService;
import com.eventhub.event_service.application.port.out.EventOutputPort;
import com.eventhub.event_service.application.port.out.EventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteEventUseCase - Testes Unitários")
class DeleteEventUseCaseTest {

    @Mock
    private EventOutputPort eventOutputPort;

    @Mock
    private EventPublisher eventPublisher;

    private DeleteEventUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new EventApplicationService(eventOutputPort, eventPublisher);
    }

    @Test
    @DisplayName("Deve deletar todos os eventos")
    void testDeleteAllSuccess() {
        // Act
        useCase.deleteAll();

        // Assert
        verify(eventOutputPort, times(1)).deleteAll();
    }

    @Test
    @DisplayName("Deve chamar outputPort ao deletar")
    void testDeleteAllCallsOutputPort() {
        // Act
        useCase.deleteAll();

        // Assert - verificar que foi chamado
        verify(eventOutputPort).deleteAll();
    }
}

