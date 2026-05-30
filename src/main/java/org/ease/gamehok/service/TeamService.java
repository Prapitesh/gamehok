package org.ease.gamehok.service;

import lombok.RequiredArgsConstructor;
import org.ease.gamehok.entity.Team;
import org.ease.gamehok.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    public Team registerTeam(Team team) {
        return teamRepository.save(team);
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }
}
