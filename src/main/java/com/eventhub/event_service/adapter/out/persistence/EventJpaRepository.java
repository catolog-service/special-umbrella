package com.eventhub.event_service.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository Spring Data JPA para EventEntity.
 * Fornece operações CRUD básicas no banco de dados.
 */
@Repository
public interface EventJpaRepository extends JpaRepository<EventEntity, UUID> {
}

