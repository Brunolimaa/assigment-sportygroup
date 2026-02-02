package com.sportygroup.bet.infrastructure.messaging.payload;

public record BetSettlementPayload(String betId, String userId, String eventId, String eventMarketId,
                                   String eventWinnerId, String betAmount, boolean won) {}