package com.eventhub.event_service.application.service;

import com.eventhub.event_service.application.port.in.CreateEventUseCase;
import com.eventhub.event_service.application.port.in.DeleteEventUseCase;
import com.eventhub.event_service.application.port.in.ListEventUsecase;
import com.eventhub.event_service.application.port.out.EventOutputPort;
import com.eventhub.event_service.domain.entity.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class EventApplicationService implements CreateEventUseCase, DeleteEventUseCase,ListEventUsecase {


    private final EventOutputPort outputPort;

    public EventApplicationService(EventOutputPort outputPort) {
        this.outputPort = outputPort;
        log.info("✅ [INIT] EventApplicationService inicializado com EventOutputPort");
    }

    @Override
    public Event create(Event event) {
        log.info("🔵 [SERVICE] Iniciando criação de evento: {} (ID: {})", event.getName(), event.getId());

        try {
            Event created = outputPort.save(event);
            log.info("✨ [SERVICE] Evento persistido com sucesso! ID: {}, Nome: {}",
                    created.getId(), created.getName());
            return created;
        } catch (Exception e) {
            log.error("❌ [SERVICE] Erro ao persistir evento: {} - {}", event.getName(), e.getMessage(), e);
            throw new RuntimeException("Erro ao criar evento: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteAll() {
        log.warn("🟡 [SERVICE] Iniciando deleção de TODOS os eventos no banco de dados");

        try {
            outputPort.deleteAll();
            log.info("✨ [SERVICE] Todos os eventos foram deletados com sucesso");
        } catch (Exception e) {
            log.error("❌ [SERVICE] Erro ao deletar eventos - {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao deletar eventos: " + e.getMessage(), e);
        }
    }


    @Override
    public List<Event>  list() {
        log.info("🔵 [SERVICE] Iniciando listagem de eventos");

        try {
            List<Event> events = outputPort.list();
            log.info("📊 [SERVICE] Total de eventos recuperados: {}", events.size());

            if (events.isEmpty()) {
                log.debug("⚠️ [SERVICE] Nenhum evento encontrado no banco de dados");
            }

            return events;
        } catch (Exception e) {
            log.error("❌ [SERVICE] Erro ao listar eventos - {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao listar eventos: " + e.getMessage(), e);
        }
    }
}

