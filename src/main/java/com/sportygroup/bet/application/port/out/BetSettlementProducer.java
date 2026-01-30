package com.sportygroup.bet.application.port.out;

import com.sportygroup.bet.domain.BetSettlement;

import java.util.List;

public interface BetSettlementProducer {
    void send(List<BetSettlement> settlements);
}
