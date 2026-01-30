package com.sportygroup.bet.application.port.out;

import com.sportygroup.bet.domain.Bet;

import java.util.List;
import java.util.Optional;

public interface BetRepository {
    Bet save(Bet bet);
    List<Bet> findByEventId(String eventId);
    List<Bet> findAll();
}
