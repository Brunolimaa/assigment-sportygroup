package com.sportygroup.bet.application.service;

import com.sportygroup.bet.application.port.out.BetRepository;
import com.sportygroup.bet.domain.Bet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("BetsService")
class BetsServiceTest {

    @Mock
    private BetRepository betRepository;

    private BetsService betsService;

    @BeforeEach
    void setUp() {
        betsService = new BetsService(betRepository);
    }

    @Test
    @DisplayName("listBets returns all bets from repository")
    void shouldListBetsReturnsAllFromRepository() {
        Bet bet = new Bet("b1", "u1", "e1", "m1", "w1", BigDecimal.TEN);
        when(betRepository.findAll()).thenReturn(List.of(bet));

        List<Bet> result = betsService.listBets();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBetId()).isEqualTo("b1");
        verify(betRepository).findAll();
    }

    @Test
    @DisplayName("listBets returns empty list when repository is empty")
    void shouldListBetsEmptyRepositoryReturnsEmptyList() {
        when(betRepository.findAll()).thenReturn(List.of());

        List<Bet> result = betsService.listBets();

        assertThat(result).isEmpty();
        verify(betRepository).findAll();
    }

    @Test
    @DisplayName("placeBet saves bet and returns saved bet")
    void shouldPlaceBetSavesAndReturnsSavedBet() {
        Bet bet = new Bet("b1", "u1", "e1", "m1", "w1", BigDecimal.TEN);
        when(betRepository.save(any(Bet.class))).thenReturn(bet);

        Bet result = betsService.placeBet(bet);

        assertThat(result).isSameAs(bet);
        assertThat(result.getBetId()).isEqualTo("b1");
        verify(betRepository).save(bet);
    }
}
