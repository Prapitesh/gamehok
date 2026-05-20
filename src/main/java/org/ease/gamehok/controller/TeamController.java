package org.ease.gamehok.controller;

import jakarta.validation.Valid;
import org.ease.gamehok.entity.Team;
import org.ease.gamehok.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping("/register")
    public Team registerTeam(@Valid @RequestBody Team team) {
        return teamService.registerTeam(team);
    }

    @GetMapping
    public List<Team> getAllTeams() {
        return teamService.getAllTeams();
    }
}