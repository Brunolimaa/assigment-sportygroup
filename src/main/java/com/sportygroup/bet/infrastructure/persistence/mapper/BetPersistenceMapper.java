package com.sportygroup.bet.infrastructure.persistence.mapper;

import com.sportygroup.bet.domain.Bet;
import com.sportygroup.bet.infrastructure.persistence.model.BetJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BetPersistenceMapper {

  BetJpaEntity toEntity(Bet domain);

  Bet toDomain(BetJpaEntity entity);
}