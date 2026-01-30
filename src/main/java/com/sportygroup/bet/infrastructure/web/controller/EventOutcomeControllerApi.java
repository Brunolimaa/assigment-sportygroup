package com.sportygroup.bet.infrastructure.web.controller;

import com.sportygroup.bet.infrastructure.web.request.EventOutcomeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Event Outcomes", description = "Endpoints for publishing sports event results to Kafka")
public interface EventOutcomeControllerApi {

  @Operation(
          summary = "Publish an event outcome",
          description = "Receives a sports event result and triggers an asynchronous notification to Kafka. This event will be consumed by the settlement system."
  )
  @ApiResponses(value = {
          @ApiResponse(
                  responseCode = "202",
                  description = "Event outcome accepted and scheduled for publication",
                  content = @Content
          ),
          @ApiResponse(
                  responseCode = "400",
                  description = "Invalid request payload or missing required fields",
                  content = @Content
          ),
          @ApiResponse(
                  responseCode = "500",
                  description = "Internal error while attempting to communicate with the message broker",
                  content = @Content
          )
  })
  ResponseEntity<Void> publish(@Valid @RequestBody EventOutcomeRequest request);
}