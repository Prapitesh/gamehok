package org.ease.gamehok.controller;

import org.ease.gamehok.dto.BracketRequest;
import org.ease.gamehok.entity.Match;
import org.ease.gamehok.entity.Team;
import org.ease.gamehok.repository.TeamRepository;
import org.ease.gamehok.service.BracketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brackets")
@RequiredArgsConstructor
public class BracketController {

    private final BracketService bracketService;
    private final TeamRepository teamRepository;
    @PostMapping("/generate")
    public List<Match> generateBracket(
            @RequestBody BracketRequest request
    ) {

        List<Team> teams =
                teamRepository.findAllById(
                        request.getTeamIds()
                );

        return bracketService.generateBracket(
                teams
        );
    }
}