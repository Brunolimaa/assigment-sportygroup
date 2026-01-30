package com.sportygroup.bet.infrastructure.web.response;

import com.sportygroup.bet.domain.Bet;

import java.math.BigDecimal;

public record BetResponse(String betId, String userId, String eventId, String eventMarketId, String eventWinnerId, BigDecimal betAmount) {
    public static BetResponse from(Bet bet) {
        return new BetResponse(
                bet.getBetId(), bet.getUserId(), bet.getEventId(), bet.getEventMarketId(),
                bet.getEventWinnerId(), bet.getBetAmount());
    }
}
