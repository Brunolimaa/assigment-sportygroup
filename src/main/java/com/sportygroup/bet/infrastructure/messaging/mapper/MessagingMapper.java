package com.sportygroup.bet.infrastructure.messaging.mapper;

import com.sportygroup.bet.domain.EventOutcome;
import com.sportygroup.bet.infrastructure.messaging.payload.EventOutcomePayload;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessagingMapper {
  EventOutcomePayload toPayload(EventOutcome domain);
  EventOutcome toDomain(EventOutcomePayload payload);
}
