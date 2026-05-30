package org.ease.gamehok.controller;

import lombok.RequiredArgsConstructor;
import org.ease.gamehok.dto.MatchResultRequest;
import org.ease.gamehok.entity.Match;
import org.ease.gamehok.entity.Team;
import org.ease.gamehok.repository.MatchRepository;
import org.ease.gamehok.repository.TeamRepository;
import org.ease.gamehok.service.MatchService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;
private final MatchRepository matchRepository;
private final TeamRepository teamRepository;
    @PostMapping("/{matchId}/result")
    public Match submitResult(
            @PathVariable Long matchId,
            @RequestBody MatchResultRequest request
    ) {

        Team winner = teamRepository.findById(
                request.getWinnerId()
        ).orElseThrow(() ->
                new RuntimeException("Team not found")
        );

        return matchService.submitResult(
                matchId,
                winner
        );
    }
    @GetMapping("/{id}")
    public Match getMatchById(@PathVariable Long id) {

        return matchService.getMatchById(id);
    }

    @GetMapping
    public Page<Match> getAllMatches(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "5")
            int size,

            @RequestParam(defaultValue = "id")
            String sortBy,

            @RequestParam(required = false)
            String status
    ) {

        return matchService.getAllMatches(
                page,
                size,
                sortBy,
                status
        );
    }
}