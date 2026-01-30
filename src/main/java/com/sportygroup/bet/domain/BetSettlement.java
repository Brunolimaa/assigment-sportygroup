package com.sportygroup.bet.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Domain value object: bet settlement to send to bet-settlements.
 */
@Getter
@Setter
@AllArgsConstructor
public final class BetSettlement {

    private final String betId;
    private final String userId;
    private final String eventId;
    private final String eventMarketId;
    private final String eventWinnerId;
    private final BigDecimal betAmount;
    private final boolean won;


    public static BetSettlement fromBetAndOutcome(Bet bet, EventOutcome outcome) {
        return new BetSettlement(
                bet.getBetId(), bet.getUserId(), bet.getEventId(), bet.getEventMarketId(),
                bet.getEventWinnerId(), bet.getBetAmount(), bet.isWonBy(outcome)
        );
    }

}
