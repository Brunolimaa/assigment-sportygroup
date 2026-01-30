package com.sportygroup.bet.infrastructure.persistence;

import com.sportygroup.bet.application.port.out.BetRepository;
import com.sportygroup.bet.domain.Bet;
import com.sportygroup.bet.infrastructure.persistence.mapper.BetPersistenceMapper;
import com.sportygroup.bet.infrastructure.persistence.model.BetJpaEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BetRepositoryAdapter implements BetRepository {

    private final BetJpaRepository jpaRepository;
    private final BetPersistenceMapper betPersistenceMapper;

    public BetRepositoryAdapter(BetJpaRepository jpaRepository, BetPersistenceMapper betPersistenceMapper) {
        this.jpaRepository = jpaRepository;
      this.betPersistenceMapper = betPersistenceMapper;
    }

    @Override
    public Bet save(Bet bet) {
        BetJpaEntity saved = jpaRepository.save(betPersistenceMapper.toEntity(bet));
        return betPersistenceMapper.toDomain(saved);
    }

    @Override
    public List<Bet> findByEventId(String eventId) {
        return jpaRepository.findByEventId(eventId).stream().map(betPersistenceMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Bet> findAll() {
        return jpaRepository.findAll().stream().map(betPersistenceMapper::toDomain).collect(Collectors.toList());
    }

}
