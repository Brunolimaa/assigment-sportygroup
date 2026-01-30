package com.sportygroup.bet.infrastructure.web.mapper;


import com.sportygroup.bet.domain.Bet;
import com.sportygroup.bet.domain.EventOutcome;
import com.sportygroup.bet.infrastructure.web.request.EventOutcomeRequest;
import com.sportygroup.bet.infrastructure.web.request.PlaceBetRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DomainMapper {

  Bet toDomain(PlaceBetRequest request);

  EventOutcome toDomain(EventOutcomeRequest request);

}