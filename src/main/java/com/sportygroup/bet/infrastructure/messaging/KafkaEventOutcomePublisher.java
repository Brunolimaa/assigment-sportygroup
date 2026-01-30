package com.sportygroup.bet.infrastructure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportygroup.bet.application.port.out.EventOutcomePublisher;
import com.sportygroup.bet.domain.EventOutcome;
import com.sportygroup.bet.infrastructure.messaging.mapper.MessagingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class KafkaEventOutcomePublisher implements EventOutcomePublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventOutcomePublisher.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final MessagingMapper messagingMapper;

    @Value("${app.kafka.topic.event-outcomes:event-outcomes}")
    private String topic;

    public KafkaEventOutcomePublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper, MessagingMapper messagingMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
      this.messagingMapper = messagingMapper;
    }

    @Override
    public void publish(EventOutcome eventOutcome) {
        String payload;
        try {
            payload = objectMapper.writeValueAsString(messagingMapper.toPayload(eventOutcome));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize event outcome", e);
        }
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, eventOutcome.getEventId(), payload);
        try {
            future.get(10, TimeUnit.SECONDS);
            log.debug("Published event outcome to Kafka: eventId={}", eventOutcome.getEventId());
        } catch (TimeoutException e) {
            log.warn("Publish to Kafka timed out: eventId={}", eventOutcome.getEventId());
        } catch (Exception e) {
            log.error("Failed to publish event outcome: eventId={}", eventOutcome.getEventId(), e);
            throw new RuntimeException("Failed to publish event outcome", e);
        }
    }

}
