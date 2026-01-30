package com.sportygroup.bet.application.service;

import com.sportygroup.bet.application.port.out.EventOutcomePublisher;
import com.sportygroup.bet.domain.EventOutcome;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("PublishEventOutcomeService")
class PublishEventOutcomeServiceTest {

    @Mock
    private EventOutcomePublisher publisher;

    private PublishEventOutcomeService publishEventOutcomeService;

    @BeforeEach
    void setUp() {
        publishEventOutcomeService = new PublishEventOutcomeService(publisher);
    }

    @Test
    @DisplayName("publish delegates to publisher")
    void shouldPublishDelegatesToPublisher() {
        EventOutcome outcome = new EventOutcome("e1", "Event 1", "winner-A");

        publishEventOutcomeService.publish(outcome);

        verify(publisher).publish(eq(outcome));
    }
}
