package com.sportygroup.bet.application.port.in;

import com.sportygroup.bet.domain.EventOutcome;

public interface PublishEventOutcomeUseCase {
    void publish(EventOutcome eventOutcome);
}
