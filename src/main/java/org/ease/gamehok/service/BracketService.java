package org.ease.gamehok.service;

import lombok.RequiredArgsConstructor;
import org.ease.gamehok.entity.Match;
import org.ease.gamehok.entity.Team;
import org.ease.gamehok.repository.MatchRepository;
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

        List<Match> firstRoundMatches = new ArrayList<>();

        // ROUND 1
        for (int i = 0; i < teams.size(); i += 2) {

            Match match = Match.builder()
                    .roundNumber(1)
                    .team1(teams.get(i))
                    .team2(teams.get(i + 1))
                    .status("PENDING")
                    .build();

            firstRoundMatches.add(match);
        }

        List<Match> savedMatches =
                matchRepository.saveAll(firstRoundMatches);

        // CREATE NEXT ROUND PLACEHOLDERS
        List<Match> nextRoundMatches = new ArrayList<>();

        for (int i = 0; i < savedMatches.size() / 2; i++) {

            Match nextRoundMatch = Match.builder()
                    .roundNumber(2)
                    .status("PENDING")
                    .build();

            nextRoundMatches.add(nextRoundMatch);
        }

        List<Match> savedNextRound =
                matchRepository.saveAll(nextRoundMatches);

        // LINK MATCHES
        int index = 0;

        for (int i = 0; i < savedMatches.size(); i += 2) {

            Match m1 = savedMatches.get(i);
            Match m2 = savedMatches.get(i + 1);

            Match next = savedNextRound.get(index++);

            m1.setNextMatchId(next.getId());
            m2.setNextMatchId(next.getId());
        }

        return matchRepository.saveAll(savedMatches);
    }
}