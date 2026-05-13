package com.eventhub.event_service.adapter.out.persistence;

import com.eventhub.event_service.application.port.out.EventOutputPort;
import com.eventhub.event_service.domain.entity.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Component
@Slf4j
public class EventRepositoryAdapter implements EventOutputPort {

    private final EventJpaRepository jpaRepository;

    public EventRepositoryAdapter(EventJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
        log.info("✅ [INIT] EventRepositoryAdapter inicializado com EventJpaRepository");
    }

    @Override
    @Transactional
    public Event save(Event event) {
        log.info("🔵 [DB] Salvando evento: {} (ID: {})", event.getName(), event.getId());

        try {
            // Domínio → Entidade JPA (ID já vem do domínio)
            EventEntity entity = new EventEntity();
            entity.setId(event.getId());
            entity.setName(event.getName());
            entity.setDescription(event.getDescription());
            entity.setDateTime(event.getDateTime());
            entity.setLocation(event.getLocation());

            log.debug("📝 [DB] Entidade JPA criada, pronto para persistir");

            EventEntity saved = jpaRepository.save(entity);
            log.info("✨ [DB] Evento persistido com sucesso no banco! ID: {}", saved.getId());

            // Entidade JPA → Domínio
            return new Event(
                    saved.getId(),
                    saved.getName(),
                    saved.getDescription(),
                    saved.getDateTime(),
                    saved.getLocation()
            );
        } catch (Exception e) {
            log.error("❌ [DB] Erro ao salvar evento: {} - {}", event.getName(), e.getMessage(), e);
            throw new RuntimeException("Erro ao persistir evento no banco: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteAll() {
        log.warn("🟡 [DB] Deletando TODOS os eventos do banco de dados");

        try {
            long countBefore = jpaRepository.count();
            log.debug("📊 [DB] Eventos antes da deleção: {}", countBefore);

            jpaRepository.deleteAll();

            long countAfter = jpaRepository.count();
            log.info("✨ [DB] Deleção concluída! Eventos deletados: {}. Eventos restantes: {}",
                    countBefore, countAfter);
        } catch (Exception e) {
            log.error("❌ [DB] Erro ao deletar eventos - {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao deletar eventos: " + e.getMessage(), e);
        }
    }


    @Override
    public List<Event> list() {
        log.info("🔵 [DB] Recuperando lista de eventos do banco de dados");

        try {
            List<EventEntity> entities = jpaRepository.findAll();
            log.info("📊 [DB] Total de eventos recuperados: {}", entities.size());

            List<Event> events = entities.stream()
                    .map(saved -> new Event(
                            saved.getId(),
                            saved.getName(),
                            saved.getDescription(),
                            saved.getDateTime(),
                            saved.getLocation()
                    ))
                    .toList();

            log.debug("✅ [DB] {} eventos mapeados do banco para domínio", events.size());
            return events;
        } catch (Exception e) {
            log.error("❌ [DB] Erro ao listar eventos - {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao listar eventos: " + e.getMessage(), e);
        }
    }
}
