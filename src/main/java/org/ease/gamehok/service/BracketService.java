package org.ease.gamehok.service;

import org.ease.gamehok.entity.Match;
import org.ease.gamehok.entity.Team;
import org.ease.gamehok.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BracketService {

    private final MatchRepository matchRepository;

    public List<Match> generateBracket(List<Team> teams) {

        Collections.shuffle(teams);

        List<Match> matches = new ArrayList<>();

        for (int i = 0; i < teams.size(); i += 2) {

            Match match = Match.builder()
                    .roundNumber(1)
                    .team1(teams.get(i))
                    .team2(teams.get(i + 1))
                    .status("PENDING")
                    .build();

            matches.add(match);
        }

        return matchRepository.saveAll(matches);
    }
}
