package com.sportygroup.bet.infrastructure.messaging;

import com.sportygroup.bet.application.port.out.BetSettlementProducer;
import com.sportygroup.bet.domain.BetSettlement;
import com.sportygroup.bet.infrastructure.messaging.mapper.MessagingMapper;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Component
@ConditionalOnProperty(name = "app.rocketmq.enabled", havingValue = "true")
public class RocketMQBetSettlementProducer implements BetSettlementProducer {

  private static final Logger log = LoggerFactory.getLogger(RocketMQBetSettlementProducer.class);
  private static final String TOPIC_BET_SETTLEMENTS = "bet-settlements";

  private final RocketMQTemplate rocketMQTemplate;
  private final MessagingMapper messagingMapper;

  public RocketMQBetSettlementProducer(RocketMQTemplate rocketMQTemplate, MessagingMapper messagingMapper) {
    this.rocketMQTemplate = rocketMQTemplate;
    this.messagingMapper = messagingMapper;
  }

  @Override
  public void send(List<BetSettlement> settlements) {
    for (BetSettlement settlement : settlements) {
      try {
        rocketMQTemplate.convertAndSend(TOPIC_BET_SETTLEMENTS, messagingMapper.toPayload(settlement));
        log.info("Successfully sent settlement to RocketMQ: betId={}", settlement.getBetId());
      } catch (Exception e) {
        log.error("Failed to send settlement to RocketMQ for betId: {}", settlement.getBetId(), e);
      }
    }
  }
}

