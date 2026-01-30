package com.sportygroup.bet.infrastructure.messaging.payload;

public record EventOutcomePayload(String eventId, String eventName, String eventWinnerId) {}
