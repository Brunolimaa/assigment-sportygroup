package com.sportygroup.bet.application.port.out;

import com.sportygroup.bet.domain.EventOutcome;

public interface EventOutcomePublisher {
    void publish(EventOutcome eventOutcome);
}
