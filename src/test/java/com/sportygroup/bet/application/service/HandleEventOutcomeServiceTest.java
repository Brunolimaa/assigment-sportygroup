package com.sportygroup.bet.application.service;

import com.sportygroup.bet.application.port.out.BetRepository;
import com.sportygroup.bet.application.port.out.BetSettlementProducer;
import com.sportygroup.bet.domain.Bet;
import com.sportygroup.bet.domain.BetSettlement;
import com.sportygroup.bet.domain.EventOutcome;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("HandleEventOutcomeService")
class HandleEventOutcomeServiceTest {

    @Mock
    private BetRepository betRepository;

    @Mock
    private BetSettlementProducer settlementProducer;

    private HandleEventOutcomeService handleEventOutcomeService;

    @BeforeEach
    void setUp() {
        handleEventOutcomeService = new HandleEventOutcomeService(betRepository, settlementProducer);
    }

    @Test
    @DisplayName("handle sends settlements when bets exist for event")
    void shouldHandleWithBetsSendsSettlements() {
        String eventId = "e1";
        Bet bet = new Bet("b1", "u1", eventId, "m1", "winner-A", BigDecimal.TEN);
        EventOutcome outcome = new EventOutcome(eventId, "Event 1", "winner-A");
        when(betRepository.findByEventId(eventId)).thenReturn(List.of(bet));

        handleEventOutcomeService.handle(outcome);

        ArgumentCaptor<List<BetSettlement>> captor = ArgumentCaptor.forClass(List.class);
        verify(settlementProducer).send(captor.capture());
        List<BetSettlement> settlements = captor.getValue();
        assertThat(settlements).hasSize(1);
        assertThat(settlements.get(0).getBetId()).isEqualTo("b1");
        assertThat(settlements.get(0).isWon()).isTrue();
    }

    @Test
    @DisplayName("handle does not call producer when no bets for event")
    void shouldHandleNoBetsDoesNotSendSettlements() {
        String eventId = "e1";
        EventOutcome outcome = new EventOutcome(eventId, "Event 1", "winner-A");
        when(betRepository.findByEventId(eventId)).thenReturn(List.of());

        handleEventOutcomeService.handle(outcome);

        verify(betRepository).findByEventId(eventId);
        verify(settlementProducer, never()).send(org.mockito.ArgumentMatchers.any());
    }

    @Test
    @DisplayName("handle marks losing bet correctly and still sends settlements")
    void shuldHandleLosingBetSendsSettlementWithWonFalse() {
        String eventId = "e1";
        Bet bet = new Bet("b1", "u1", eventId, "m1", "winner-A", BigDecimal.TEN);
        EventOutcome outcome = new EventOutcome(eventId, "Event 1", "winner-B"); // different winner
        when(betRepository.findByEventId(eventId)).thenReturn(List.of(bet));

        handleEventOutcomeService.handle(outcome);

        ArgumentCaptor<List<BetSettlement>> captor = ArgumentCaptor.forClass(List.class);
        verify(settlementProducer).send(captor.capture());
        assertThat(captor.getValue().get(0).isWon()).isFalse();
    }
}
