package com.sportygroup.bet.application.port.in;

import com.sportygroup.bet.domain.Bet;

import java.util.List;

public interface BetsUseCase {
    List<Bet> listBets();

    Bet placeBet(Bet bet);
}
