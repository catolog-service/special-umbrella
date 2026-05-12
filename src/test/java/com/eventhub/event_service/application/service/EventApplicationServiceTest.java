package com.eventhub.event_service.application.service;

import com.eventhub.event_service.application.port.out.EventOutputPort;
import com.eventhub.event_service.domain.entity.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("EventApplicationService Tests")
class EventApplicationServiceTest {

    @Mock
    private EventOutputPort eventOutputPort;

    @InjectMocks
    private EventApplicationService eventApplicationService;

    private Event testEvent;
    private UUID testEventId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testEventId = UUID.randomUUID();
        testEvent = new Event(
            testEventId,
            "Test Event",
            "Test Description",
            LocalDateTime.now(),
            "Test Location"
        );
    }

    @Test
    @DisplayName("Should create an event successfully")
    void testCreateEventSuccess() {
        // Arrange
        Event expectedEvent = new Event(
            testEventId,
            "Test Event",
            "Test Description",
            LocalDateTime.now(),
            "Test Location"
        );
        when(eventOutputPort.save(testEvent)).thenReturn(expectedEvent);

        // Act
        Event result = eventApplicationService.create(testEvent);

        // Assert
        assertNotNull(result);
        assertEquals(testEventId, result.getId());
        assertEquals("Test Event", result.getName());
        assertEquals("Test Description", result.getDescription());
        assertEquals("Test Location", result.getLocation());
        verify(eventOutputPort, times(1)).save(testEvent);
    }

    @Test
    @DisplayName("Should create an event with all fields populated")
    void testCreateEventWithAllFields() {
        // Arrange
        LocalDateTime eventDateTime = LocalDateTime.of(2026, 5, 20, 10, 0);
        Event eventWithAllFields = new Event(
            testEventId,
            "Conference",
            "Tech Conference 2026",
            eventDateTime,
            "São Paulo, Brazil"
        );
        when(eventOutputPort.save(eventWithAllFields)).thenReturn(eventWithAllFields);

        // Act
        Event result = eventApplicationService.create(eventWithAllFields);

        // Assert
        assertNotNull(result);
        assertEquals("Conference", result.getName());
        assertEquals("Tech Conference 2026", result.getDescription());
        assertEquals(eventDateTime, result.getDateTime());
        assertEquals("São Paulo, Brazil", result.getLocation());
    }

    @Test
    @DisplayName("Should call outputPort.save exactly once during create")
    void testCreateCallsOutputPortOnce() {
        // Arrange
        when(eventOutputPort.save(testEvent)).thenReturn(testEvent);

        // Act
        eventApplicationService.create(testEvent);

        // Assert
        verify(eventOutputPort, times(1)).save(testEvent);
        verifyNoMoreInteractions(eventOutputPort);
    }

    @Test
    @DisplayName("Should delete all events successfully")
    void testDeleteAllSuccess() {
        // Arrange
        doNothing().when(eventOutputPort).deleteAll();

        // Act
        eventApplicationService.deleteAll();

        // Assert
        verify(eventOutputPort, times(1)).deleteAll();
    }

    @Test
    @DisplayName("Should call outputPort.deleteAll exactly once")
    void testDeleteAllCallsOutputPortOnce() {
        // Arrange
        doNothing().when(eventOutputPort).deleteAll();

        // Act
        eventApplicationService.deleteAll();

        // Assert
        verify(eventOutputPort, times(1)).deleteAll();
        verifyNoMoreInteractions(eventOutputPort);
    }

    @Test
    @DisplayName("Should list all events successfully")
    void testListEventsSuccess() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Event event1 = new Event(UUID.randomUUID(), "Event 1", "Description 1", now, "Location 1");
        Event event2 = new Event(UUID.randomUUID(), "Event 2", "Description 2", now, "Location 2");
        List<Event> expectedEvents = Arrays.asList(event1, event2);

        when(eventOutputPort.list()).thenReturn(expectedEvents);

        // Act
        List<Event> result = eventApplicationService.list();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Event 1", result.get(0).getName());
        assertEquals("Event 2", result.get(1).getName());
        verify(eventOutputPort, times(1)).list();
    }

    @Test
    @DisplayName("Should return empty list when no events exist")
    void testListEventsEmpty() {
        // Arrange
        when(eventOutputPort.list()).thenReturn(Collections.emptyList());

        // Act
        List<Event> result = eventApplicationService.list();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
        verify(eventOutputPort, times(1)).list();
    }

    @Test
    @DisplayName("Should return list with single event")
    void testListEventsSingleEvent() {
        // Arrange
        List<Event> expectedEvents = Collections.singletonList(testEvent);
        when(eventOutputPort.list()).thenReturn(expectedEvents);

        // Act
        List<Event> result = eventApplicationService.list();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testEvent.getName(), result.get(0).getName());
        verify(eventOutputPort, times(1)).list();
    }

    @Test
    @DisplayName("Should call outputPort.list exactly once during list operation")
    void testListCallsOutputPortOnce() {
        // Arrange
        when(eventOutputPort.list()).thenReturn(Collections.emptyList());

        // Act
        eventApplicationService.list();

        // Assert
        verify(eventOutputPort, times(1)).list();
        verifyNoMoreInteractions(eventOutputPort);
    }

    @Test
    @DisplayName("Should throw exception when event is null")
    void testCreateWithNullEventThrowsException() {
        // Arrange & Act & Assert
        assertThrows(NullPointerException.class, () -> {
            eventApplicationService.create(null);
        });
    }

    @Test
    @DisplayName("Should maintain event properties after creation")
    void testCreatePreservesEventProperties() {
        // Arrange
        LocalDateTime eventTime = LocalDateTime.of(2026, 6, 15, 14, 30);
        Event event = new Event(
            testEventId,
            "Workshop",
            "Java Workshop",
            eventTime,
            "Online"
        );
        when(eventOutputPort.save(event)).thenReturn(event);

        // Act
        Event result = eventApplicationService.create(event);

        // Assert
        assertEquals(event.getId(), result.getId());
        assertEquals(event.getName(), result.getName());
        assertEquals(event.getDescription(), result.getDescription());
        assertEquals(event.getDateTime(), result.getDateTime());
        assertEquals(event.getLocation(), result.getLocation());
    }
}

