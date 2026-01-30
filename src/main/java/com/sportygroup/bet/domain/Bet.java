package com.sportygroup.bet.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Domain entity: a bet (Bet ID, User ID, Event ID, Event Market ID, Event Winner ID, Bet Amount).
 */
@Getter
@Setter
@AllArgsConstructor
public final class Bet {

    private final String betId;
    private final String userId;
    private final String eventId;
    private final String eventMarketId;
    private final String eventWinnerId;
    private final BigDecimal betAmount;

    public boolean isWonBy(EventOutcome outcome) {
        return this.eventId.equals(outcome.getEventId()) && this.eventWinnerId.equals(outcome.getEventWinnerId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bet bet = (Bet) o;
        return betId.equals(bet.betId);
    }

    @Override
    public int hashCode() { return Objects.hash(betId); }
}
