package com.sportygroup.bet.application.service;

import com.sportygroup.bet.application.port.in.BetsUseCase;
import com.sportygroup.bet.application.port.out.BetRepository;
import com.sportygroup.bet.domain.Bet;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BetsService implements BetsUseCase {

    private final BetRepository betRepository;

    public BetsService(BetRepository betRepository) {
        this.betRepository = betRepository;
    }

    @Override
    public List<Bet> listBets() {
        return betRepository.findAll();
    }

    @Override
    public Bet placeBet(Bet bet) {
      return betRepository.save(bet);
    }
}
