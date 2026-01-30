package com.sportygroup.bet.infrastructure.persistence;

import com.sportygroup.bet.infrastructure.persistence.model.BetJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BetJpaRepository extends JpaRepository<BetJpaEntity, String> {
    List<BetJpaEntity> findByEventId(String eventId);
}
