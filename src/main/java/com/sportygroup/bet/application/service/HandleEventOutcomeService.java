package com.sportygroup.bet.application.service;

import com.sportygroup.bet.application.port.in.HandleEventOutcomeUseCase;
import com.sportygroup.bet.application.port.out.BetRepository;
import com.sportygroup.bet.application.port.out.BetSettlementProducer;
import com.sportygroup.bet.domain.Bet;
import com.sportygroup.bet.domain.BetSettlement;
import com.sportygroup.bet.domain.EventOutcome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HandleEventOutcomeService implements HandleEventOutcomeUseCase {

    private static final Logger log = LoggerFactory.getLogger(HandleEventOutcomeService.class);

    private final BetRepository betRepository;
    private final BetSettlementProducer settlementProducer;

    public HandleEventOutcomeService(BetRepository betRepository, BetSettlementProducer settlementProducer) {
        this.betRepository = betRepository;
        this.settlementProducer = settlementProducer;
    }

    @Override
    public void handle(EventOutcome eventOutcome) {
        String eventId = eventOutcome.getEventId();
        List<Bet> betsForEvent = betRepository.findByEventId(eventId);

        if (betsForEvent.isEmpty()) {
          log.info("[Settlement] No bets found for eventId: {}", eventId);
          return;
        }

        List<BetSettlement> settlements = betsForEvent.stream()
                .map(bet -> BetSettlement.fromBetAndOutcome(bet, eventOutcome))
                .collect(Collectors.toList());


      long winnersCount = settlements.stream().filter(BetSettlement::isWon).count();

      if (winnersCount > 0) {
        log.info("[Settlement] Processing results for event {}: {} total bets, {} winners found.",
                eventId, settlements.size(), winnersCount);
        settlementProducer.send(settlements);
      } else {
        log.info("[Settlement] Processing results for event {}: {} total bets, but no winners this time.",
                eventId, settlements.size());
        settlementProducer.send(settlements);
      }
    }
}
