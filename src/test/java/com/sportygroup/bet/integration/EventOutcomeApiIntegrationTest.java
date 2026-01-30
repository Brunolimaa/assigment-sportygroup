package com.sportygroup.bet.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportygroup.bet.infrastructure.messaging.EventOutcomesKafkaConsumer;
import com.sportygroup.bet.infrastructure.messaging.KafkaEventOutcomePublisher;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the Event Outcome API with full Spring context.
 * Kafka publisher is mocked so no broker is required.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration"
})
@DisplayName("Event Outcome API Integration")
class EventOutcomeApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private KafkaEventOutcomePublisher kafkaEventOutcomePublisher;

    @MockitoBean
    private EventOutcomesKafkaConsumer eventOutcomesKafkaConsumer;

    @Test
    @DisplayName("POST /api/event-outcomes returns 202 Accepted")
    void shouldPublishEventOutcomeReturnsAccepted() throws Exception {
        Map<String, String> request = Map.of(
                "eventId", "event-1",
                "eventName", "Match Alpha vs Beta",
                "eventWinnerId", "winner-A"
        );

        mockMvc.perform(post("/api/event-outcomes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted());
    }

    @Test
    @DisplayName("POST /api/event-outcomes with missing fields returns 400")
    void shouldPublishEventOutcomeInvalidReturnsBadRequest() throws Exception {
        Map<String, String> invalidRequest = Map.of(
                "eventId", "event-1"
        );

        mockMvc.perform(post("/api/event-outcomes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}
