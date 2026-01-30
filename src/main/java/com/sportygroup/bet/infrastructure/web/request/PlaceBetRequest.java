package com.sportygroup.bet.infrastructure.web.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PlaceBetRequest(
        @NotBlank(message = "betId is required") String betId,
        @NotBlank(message = "userId is required") String userId,
        @NotBlank(message = "eventId is required") String eventId,
        @NotBlank(message = "eventMarketId is required") String eventMarketId,
        @NotBlank(message = "eventWinnerId is required") String eventWinnerId,
        @NotNull @DecimalMin(value = "0.01", message = "betAmount must be positive") BigDecimal betAmount
) {}
