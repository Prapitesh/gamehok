package org.ease.gamehok.service;

import lombok.RequiredArgsConstructor;
import org.ease.gamehok.dto.TeamRegistrationRequest;
import org.ease.gamehok.entity.Team;
import org.ease.gamehok.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    public Team registerTeam(TeamRegistrationRequest request) {

        Team team = Team.builder()
                .teamName(request.getTeamName())
                .logoUrl(request.getLogoUrl())
                .build();

        return teamRepository.save(team);
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }
}