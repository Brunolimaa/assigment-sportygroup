package com.sportygroup.bet.infrastructure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportygroup.bet.application.port.in.BetsUseCase;
import com.sportygroup.bet.domain.Bet;
import com.sportygroup.bet.infrastructure.web.mapper.DomainMapper;
import com.sportygroup.bet.infrastructure.web.request.PlaceBetRequest;
import com.sportygroup.bet.infrastructure.web.response.BetResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BetController")
class BetControllerTest {

    @Mock
    private BetsUseCase betsUseCase;

    @Mock
    private DomainMapper domainMapper;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        BetController controller = new BetController(betsUseCase, domainMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("POST /api/bets returns 201 and saved bet")
    void shouldPlaceBetReturnsCreated() throws Exception {
        PlaceBetRequest request = new PlaceBetRequest(
                "b1", "u1", "e1", "m1", "w1", new BigDecimal("10.00"));
        Bet domainBet = new Bet("b1", "u1", "e1", "m1", "w1", new BigDecimal("10.00"));
        when(domainMapper.toDomain(request)).thenReturn(domainBet);
        when(betsUseCase.placeBet(any(Bet.class))).thenReturn(domainBet);

        mockMvc.perform(post("/api/bets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.betId").value("b1"))
                .andExpect(jsonPath("$.userId").value("u1"))
                .andExpect(jsonPath("$.eventId").value("e1"))
                .andExpect(jsonPath("$.betAmount").value(10.0));
    }

    @Test
    @DisplayName("GET /api/bets returns 200 and list of bets")
    void shouldListBetsReturnsOkWithList() throws Exception {
        Bet bet = new Bet("b1", "u1", "e1", "m1", "w1", BigDecimal.TEN);
        when(betsUseCase.listBets()).thenReturn(List.of(bet));

        mockMvc.perform(get("/api/bets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].betId").value("b1"))
                .andExpect(jsonPath("$[0].userId").value("u1"));
    }

    @Test
    @DisplayName("GET /api/bets returns empty array when no bets")
    void shouldListBetsEmptyReturnsEmptyArray() throws Exception {
        when(betsUseCase.listBets()).thenReturn(List.of());

        mockMvc.perform(get("/api/bets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}
