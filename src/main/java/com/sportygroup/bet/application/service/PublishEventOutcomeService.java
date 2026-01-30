package com.sportygroup.bet.application.service;

import com.sportygroup.bet.application.port.in.PublishEventOutcomeUseCase;
import com.sportygroup.bet.application.port.out.EventOutcomePublisher;
import com.sportygroup.bet.domain.EventOutcome;
import org.springframework.stereotype.Service;

@Service
public class PublishEventOutcomeService implements PublishEventOutcomeUseCase {

    private final EventOutcomePublisher publisher;

    public PublishEventOutcomeService(EventOutcomePublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(EventOutcome eventOutcome) {
        publisher.publish(eventOutcome);
    }
}
