package com.sportygroup.bet.infrastructure.web.request;

import jakarta.validation.constraints.NotBlank;

public record EventOutcomeRequest(
        @NotBlank(message = "eventId is required") String eventId,
        @NotBlank(message = "eventName is required") String eventName,
        @NotBlank(message = "eventWinnerId is required") String eventWinnerId
) {}
