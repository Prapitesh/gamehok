package org.ease.gamehok.repository;

import org.ease.gamehok.entity.Match;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MatchRepository
        extends JpaRepository<Match, Long> {

    Page<Match> findByStatus(
            String status,
            Pageable pageable
    );

    @Query("""
       SELECT m
       FROM Match m
       JOIN FETCH m.team1
       JOIN FETCH m.team2
       """)
    List<Match> findAllWithTeams();

    List<Match> findByRoundNumber(Integer roundNumber);
}
