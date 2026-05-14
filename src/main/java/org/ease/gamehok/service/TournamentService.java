package org.ease.gamehok.service;

import org.ease.gamehok.entity.Tournament;
import org.ease.gamehok.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TournamentService {

    private final TournamentRepository tournamentRepository;

    public Tournament createTournament(Tournament tournament) {
        tournament.setStatus("UPCOMING");
        return tournamentRepository.save(tournament);
    }
}
