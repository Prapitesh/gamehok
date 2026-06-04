package org.ease.gamehok.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ease.gamehok.dto.TournamentRequest;
import org.ease.gamehok.dto.TournamentResponse;
import org.ease.gamehok.service.TournamentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
public class TournamentController {

    private final TournamentService tournamentService;

    @PostMapping
    public TournamentResponse createTournament(@Valid @RequestBody TournamentRequest tournamentRequest) {
        return tournamentService.createTournament(tournamentRequest);
    }

    @GetMapping
    public ResponseEntity<List<TournamentResponse>> getAllTournaments() {
        return ResponseEntity.ok(tournamentService.getAllTournaments());
    }
}
