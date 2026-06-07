package org.ease.gamehok.service;

import org.ease.gamehok.dto.RegistrationRequest;
import org.ease.gamehok.entity.Team;
import org.ease.gamehok.entity.Tournament;
import org.ease.gamehok.entity.TournamentRegistration;
import org.ease.gamehok.repository.TeamRepository;
import org.ease.gamehok.repository.TournamentRegistrationRepository;
import org.ease.gamehok.repository.TournamentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TournamentRegistrationService {

    private final TournamentRegistrationRepository registrationRepository;
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;

    public TournamentRegistrationService(
            TournamentRegistrationRepository registrationRepository,
            TournamentRepository tournamentRepository,
            TeamRepository teamRepository) {

        this.registrationRepository = registrationRepository;
        this.tournamentRepository = tournamentRepository;
        this.teamRepository = teamRepository;
    }

    public TournamentRegistration registerTeam(
            RegistrationRequest request) {

        if (registrationRepository.existsByTournamentIdAndTeamId(
                request.getTournamentId(),
                request.getTeamId())) {

            throw new RuntimeException(
                    "Team already registered for tournament");
        }

        Tournament tournament =
                tournamentRepository.findById(
                                request.getTournamentId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Tournament not found"));

        Team team =
                teamRepository.findById(
                                request.getTeamId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Team not found"));

        TournamentRegistration registration =
                new TournamentRegistration();

        registration.setTournament(tournament);
        registration.setTeam(team);
        registration.setRegisteredAt(LocalDateTime.now());

        return registrationRepository.save(registration);
    }

    public List<TournamentRegistration> getRegisteredTeams(
            Long tournamentId) {

        return registrationRepository
                .findByTournamentId(tournamentId);
    }

    public void deleteRegistration(Long id) {

        registrationRepository.deleteById(id);
    }
}