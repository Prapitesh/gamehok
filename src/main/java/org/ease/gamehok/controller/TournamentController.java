package org.ease.gamehok.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ease.gamehok.entity.Tournament;
import org.ease.gamehok.service.TournamentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
public class TournamentController {

    private final TournamentService tournamentService;

    @PostMapping
    public Tournament createTournament(@Valid @RequestBody Tournament tournament) {
        return tournamentService.createTournament(tournament);
    }
}
