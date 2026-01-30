package com.sportygroup.bet.infrastructure.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "bets")
public class BetJpaEntity {

    @Id
    @Column(name = "bet_id", nullable = false, length = 255)
    private String betId;

    @Column(name = "user_id", nullable = false, length = 255)
    private String userId;

    @Column(name = "event_id", nullable = false, length = 255)
    private String eventId;

    @Column(name = "event_market_id", nullable = false, length = 255)
    private String eventMarketId;

    @Column(name = "event_winner_id", nullable = false, length = 255)
    private String eventWinnerId;

    @Column(name = "bet_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal betAmount;

    protected BetJpaEntity() {}

    public BetJpaEntity(String betId, String userId, String eventId, String eventMarketId,
                        String eventWinnerId, BigDecimal betAmount) {
        this.betId = betId;
        this.userId = userId;
        this.eventId = eventId;
        this.eventMarketId = eventMarketId;
        this.eventWinnerId = eventWinnerId;
        this.betAmount = betAmount;
    }

    public String getBetId() { return betId; }
    public String getUserId() { return userId; }
    public String getEventId() { return eventId; }
    public String getEventMarketId() { return eventMarketId; }
    public String getEventWinnerId() { return eventWinnerId; }
    public BigDecimal getBetAmount() { return betAmount; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BetJpaEntity that = (BetJpaEntity) o;
        return Objects.equals(betId, that.betId);
    }

    @Override
    public int hashCode() { return Objects.hash(betId); }
}
