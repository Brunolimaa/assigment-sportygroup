package com.sportygroup.bet.infrastructure.web.controller;

import com.sportygroup.bet.application.port.in.PublishEventOutcomeUseCase;
import com.sportygroup.bet.infrastructure.web.mapper.DomainMapper;
import com.sportygroup.bet.infrastructure.web.request.EventOutcomeRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/event-outcomes")
public class EventOutcomeController implements EventOutcomeControllerApi{

    private final PublishEventOutcomeUseCase publishEventOutcomeUseCase;
    private final DomainMapper domainMapper;

    public EventOutcomeController(PublishEventOutcomeUseCase publishEventOutcomeUseCase, DomainMapper domainMapper) {
        this.publishEventOutcomeUseCase = publishEventOutcomeUseCase;
        this.domainMapper = domainMapper;
    }

    @Override
    @PostMapping
    public ResponseEntity<Void> publish(@Valid @RequestBody EventOutcomeRequest request) {
        publishEventOutcomeUseCase.publish(domainMapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
