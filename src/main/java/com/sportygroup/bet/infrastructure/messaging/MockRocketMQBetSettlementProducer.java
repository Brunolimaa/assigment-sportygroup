package com.sportygroup.bet.infrastructure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportygroup.bet.application.port.out.BetSettlementProducer;
import com.sportygroup.bet.domain.BetSettlement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConditionalOnProperty(name = "app.rocketmq.enabled", havingValue = "false", matchIfMissing = true)
public class MockRocketMQBetSettlementProducer implements BetSettlementProducer {

    private static final Logger log = LoggerFactory.getLogger(MockRocketMQBetSettlementProducer.class);
    private static final String TOPIC_BET_SETTLEMENTS = "bet-settlements";

    private final ObjectMapper objectMapper;

    public MockRocketMQBetSettlementProducer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void send(List<BetSettlement> settlements) {
        for (BetSettlement s : settlements) {
            try {
                String payload = objectMapper.writeValueAsString(new BetSettlementPayload(
                        s.getBetId(), s.getUserId(), s.getEventId(), s.getEventMarketId(),
                        s.getEventWinnerId(), s.getBetAmount().toString(), s.isWon()));
                log.info("[Mock RocketMQ] Would send to topic '{}': {}", TOPIC_BET_SETTLEMENTS, payload);
            } catch (JsonProcessingException e) {
                log.info("[Mock RocketMQ] Would send to topic '{}': betId={}, won={}", TOPIC_BET_SETTLEMENTS, s.getBetId(), s.isWon());
            }
        }
    }

    public record BetSettlementPayload(String betId, String userId, String eventId, String eventMarketId,
                                      String eventWinnerId, String betAmount, boolean won) {}
}
