package org.ease.gamehok.repository;

import org.ease.gamehok.entity.TournamentRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TournamentRegistrationRepository
        extends JpaRepository<TournamentRegistration, Long> {

    List<TournamentRegistration> findByTournamentId(Long tournamentId);

    boolean existsByTournamentIdAndTeamId(Long tournamentId,
                                          Long teamId);
}