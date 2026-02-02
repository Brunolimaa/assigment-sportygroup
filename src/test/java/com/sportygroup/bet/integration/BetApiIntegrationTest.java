package com.sportygroup.bet.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportygroup.bet.infrastructure.messaging.EventOutcomesKafkaConsumer;
import com.sportygroup.bet.infrastructure.messaging.KafkaEventOutcomePublisher;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the Bets API: real Spring context, H2 database, controllers, services, and repository.
 * Kafka-dependent beans are mocked so no broker is required.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.autoconfigure.exclude[0]=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration",
        "spring.autoconfigure.exclude[1]=org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration",
        "app.rocketmq.enabled=true"
})
@DisplayName("Bet API Integration")
class BetApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private KafkaEventOutcomePublisher kafkaEventOutcomePublisher;

    @MockitoBean
    private EventOutcomesKafkaConsumer eventOutcomesKafkaConsumer;

    @MockitoBean
    private RocketMQTemplate rocketMQTemplate;

    @Test
    @DisplayName("POST /api/bets then GET /api/bets returns created bet")
    void shouldPlaceBetThenListReturnsCreatedBet() throws Exception {
        Map<String, Object> placeBetRequest = Map.of(
                "betId", "bet-int-1",
                "userId", "user-1",
                "eventId", "event-1",
                "eventMarketId", "market-1",
                "eventWinnerId", "winner-A",
                "betAmount", 15.50
        );

        mockMvc.perform(post("/api/bets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(placeBetRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.betId").value("bet-int-1"))
                .andExpect(jsonPath("$.userId").value("user-1"))
                .andExpect(jsonPath("$.eventId").value("event-1"))
                .andExpect(jsonPath("$.betAmount").value(15.5));

        mockMvc.perform(get("/api/bets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].betId").value("bet-int-1"))
                .andExpect(jsonPath("$[0].userId").value("user-1"));
    }

    @Test
    @DisplayName("POST /api/bets with invalid body returns 400")
    void shouldPlaceBetInvalidBodyReturnsBadRequest() throws Exception {
        Map<String, Object> invalidRequest = Map.of(
                "betId", "",
                "userId", "user-1",
                "eventId", "event-1",
                "eventMarketId", "market-1",
                "eventWinnerId", "winner-A",
                "betAmount", 0
        );

        mockMvc.perform(post("/api/bets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}
