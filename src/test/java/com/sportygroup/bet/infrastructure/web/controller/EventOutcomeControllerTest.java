package com.sportygroup.bet.infrastructure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportygroup.bet.application.port.in.PublishEventOutcomeUseCase;
import com.sportygroup.bet.infrastructure.web.mapper.DomainMapper;
import com.sportygroup.bet.infrastructure.web.request.EventOutcomeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("EventOutcomeController")
class EventOutcomeControllerTest {

    @Mock
    private PublishEventOutcomeUseCase publishEventOutcomeUseCase;

    @Mock
    private DomainMapper domainMapper;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        EventOutcomeController controller = new EventOutcomeController(publishEventOutcomeUseCase, domainMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("POST /api/event-outcomes returns 202 and delegates to use case")
    void shouldPublishReturnsAccepted() throws Exception {
        EventOutcomeRequest request = new EventOutcomeRequest("e1", "Event 1", "winner-A");

        mockMvc.perform(post("/api/event-outcomes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted());

        verify(domainMapper).toDomain(request);
        verify(publishEventOutcomeUseCase).publish(any());
    }
}
