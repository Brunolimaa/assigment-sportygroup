package com.sportygroup.bet.infrastructure.web.controller;

import com.sportygroup.bet.infrastructure.web.request.PlaceBetRequest;
import com.sportygroup.bet.infrastructure.web.response.BetResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Bet Management", description = "Operations related to placing and retrieving bets")
public interface BetControllerApi {

  @Operation(
          summary = "Place a new bet",
          description = "Creates a new bet in the system. The event must be active for the bet to be accepted."
  )
  @ApiResponses(value = {
          @ApiResponse(
                  responseCode = "201",
                  description = "Bet successfully created",
                  content = @Content(schema = @Schema(implementation = BetResponse.class))
          ),
          @ApiResponse(
                  responseCode = "400",
                  description = "Invalid input data or business rule violation",
                  content = @Content
          ),
          @ApiResponse(
                  responseCode = "500",
                  description = "Internal server error",
                  content = @Content
          )
  })
  ResponseEntity<BetResponse> placeBet(@Valid @RequestBody PlaceBetRequest request);

  @Operation(
          summary = "Get all bets",
          description = "Returns a list of all bets currently stored in the database."
  )
  @ApiResponse(
          responseCode = "200",
          description = "List of bets retrieved successfully",
          content = @Content(schema = @Schema(implementation = BetResponse.class))
  )
  ResponseEntity<List<BetResponse>> listBets();
}