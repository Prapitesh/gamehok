package org.ease.gamehok.service;

import lombok.RequiredArgsConstructor;
import org.ease.gamehok.dto.TournamentRequest;
import org.ease.gamehok.dto.TournamentResponse;
import org.ease.gamehok.entity.Tournament;
import org.ease.gamehok.repository.TournamentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentService {

    private final TournamentRepository tournamentRepository;

    public TournamentResponse createTournament(TournamentRequest request) {

        Tournament tournament = Tournament.builder()
                .name(request.getName())
                .game(request.getGame())
                .mode(request.getMode())
                .status("UPCOMING")
                .prizePool(request.getPrizePool())
                .bannerUrl(request.getBannerUrl())
                .build();

        Tournament saved = tournamentRepository.save(tournament);

        return mapToResponse(saved);
    }
    private TournamentResponse mapToResponse(Tournament tournament) {

        return TournamentResponse.builder()
                .id(tournament.getId())
                .name(tournament.getName())
                .game(tournament.getGame())
                .mode(tournament.getMode())
                .status(tournament.getStatus())
                .prizePool(tournament.getPrizePool())
                .bannerUrl(tournament.getBannerUrl())
                .build();
    }

    public List<TournamentResponse> getAllTournaments() {

        return tournamentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
}
