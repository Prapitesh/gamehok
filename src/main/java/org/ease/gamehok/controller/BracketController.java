package org.ease.gamehok.controller;

import org.ease.gamehok.entity.Match;
import org.ease.gamehok.entity.Team;
import org.ease.gamehok.service.BracketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brackets")
@RequiredArgsConstructor
public class BracketController {

    private final BracketService bracketService;

    @PostMapping("/generate")
    public List<Match> generateBracket(@RequestBody List<Team> teams) {
        return bracketService.generateBracket(teams);
    }
}