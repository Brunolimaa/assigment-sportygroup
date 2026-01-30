package com.sportygroup.bet.infrastructure.web.controller;

import com.sportygroup.bet.application.port.in.BetsUseCase;
import com.sportygroup.bet.domain.Bet;
import com.sportygroup.bet.infrastructure.web.mapper.DomainMapper;
import com.sportygroup.bet.infrastructure.web.request.PlaceBetRequest;
import com.sportygroup.bet.infrastructure.web.response.BetResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bets")
public class BetController implements BetControllerApi {

    private final BetsUseCase betsUseCase;
    private final DomainMapper domainMapper;

    public BetController(BetsUseCase betsUseCase, DomainMapper domainMapper) {
        this.betsUseCase = betsUseCase;
      this.domainMapper = domainMapper;
    }

    @Override
    @PostMapping
    public ResponseEntity<BetResponse> placeBet(@Valid @RequestBody PlaceBetRequest request) {
        Bet saved = betsUseCase.placeBet(domainMapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(BetResponse.from(saved));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<BetResponse>> listBets() {
        List<BetResponse> list = betsUseCase.listBets().stream().map(BetResponse::from).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }
}
