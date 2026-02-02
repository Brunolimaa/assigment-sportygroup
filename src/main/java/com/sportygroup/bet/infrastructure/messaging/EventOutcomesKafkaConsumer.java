package com.sportygroup.bet.infrastructure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportygroup.bet.application.port.in.HandleEventOutcomeUseCase;
import com.sportygroup.bet.domain.EventOutcome;
import com.sportygroup.bet.infrastructure.messaging.mapper.MessagingMapper;
import com.sportygroup.bet.infrastructure.messaging.payload.EventOutcomePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EventOutcomesKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(EventOutcomesKafkaConsumer.class);

    private final HandleEventOutcomeUseCase handleEventOutcomeUseCase;
    private final ObjectMapper objectMapper;
    private final MessagingMapper messagingMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public EventOutcomesKafkaConsumer(HandleEventOutcomeUseCase handleEventOutcomeUseCase, ObjectMapper objectMapper, MessagingMapper messagingMapper, KafkaTemplate<String, String> kafkaTemplate) {
        this.handleEventOutcomeUseCase = handleEventOutcomeUseCase;
        this.objectMapper = objectMapper;
      this.messagingMapper = messagingMapper;
      this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(
            topics = "${app.kafka.topic.event-outcomes:event-outcomes}",
            groupId = "${spring.kafka.consumer.group-id:bet-settlement-group}",
            id = "event-outcomes-consumer",
            containerFactory = "eventOutcomesContainerFactory"
    )
    public void consume(@Payload String message,
                        @Header(value = KafkaHeaders.RECEIVED_TOPIC, required = false) String topic,
                        @Header(value = KafkaHeaders.OFFSET, required = false) Long offset) {
        log.info("Received event outcome from Kafka: topic={}, offset={}, payload={}", topic, offset, message);
        try {
            EventOutcomePayload payload = objectMapper.readValue(message, EventOutcomePayload.class);
            handleEventOutcomeUseCase.handle(messagingMapper.toDomain(payload));
        } catch (JsonProcessingException e) {
          log.error("Invalid JSON, sending to DLQ: {}", message, e);
          kafkaTemplate.send("event-outcomes-dlq", message);
        } catch (Exception e) {
            log.error("Failed to process event outcome: {}", message, e);
            throw new RuntimeException("Event outcome processing failed", e);
        }
    }

}
