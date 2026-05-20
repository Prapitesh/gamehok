package org.ease.gamehok.service;


import lombok.extern.slf4j.Slf4j;
import org.ease.gamehok.dto.MatchResponseDto;
import org.ease.gamehok.entity.Match;
import org.ease.gamehok.entity.Team;
import org.ease.gamehok.exception.ResourceNotFoundException;
import org.ease.gamehok.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final RedisMatchCacheService redisMatchCacheService;

    public Match getMatchById(Long matchId) {

        Match cachedMatch = null;

        try {
            cachedMatch =
                    redisMatchCacheService.getMatch(matchId);
        } catch (Exception e) {
            System.out.println("Redis unavailable");
        }

        if (cachedMatch != null) {
            log.info("Fetched from Redis Cache");
            return cachedMatch;
        }

        Match dbMatch = matchRepository.findById(matchId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Match not found"
                        )
                );

        try {
            redisMatchCacheService.saveMatch(dbMatch);
        } catch (Exception e) {
            System.out.println("Redis unavailable");
        }

        log.info("Fetched from PostgreSQL");

        return dbMatch;
    }

    public Match submitResult(Long matchId, Team winner) {

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Match not found"
                        )
                );

        match.setWinner(winner);
        match.setStatus("COMPLETED");

        Match updatedMatch = matchRepository.save(match);

        // MOVE WINNER TO NEXT MATCH
        if (updatedMatch.getNextMatchId() != null) {

            Match nextMatch = matchRepository.findById(
                    updatedMatch.getNextMatchId()
            ).orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Next match not found"
                    )
            );

            if (nextMatch.getTeam1() == null) {
                nextMatch.setTeam1(winner);
            } else {
                nextMatch.setTeam2(winner);
            }

            matchRepository.save(nextMatch);
        }

        redisMatchCacheService.saveMatch(updatedMatch);

        return updatedMatch;
    }

    public Page<Match> getAllMatches(
            int page,
            int size,
            String sortBy,
            String status
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortBy)
        );

        if (status != null && !status.isEmpty()) {
            return matchRepository.findByStatus(
                    status,
                    pageable
            );
        }

        return matchRepository.findAll(pageable);
    }

    public MatchResponseDto mapToDto(Match match) {

        return MatchResponseDto.builder()
                .id(match.getId())
                .roundNumber(match.getRoundNumber())
                .team1(match.getTeam1().getTeamName())
                .team2(match.getTeam2().getTeamName())
                .winner(
                        match.getWinner() != null
                                ? match.getWinner()
                                .getTeamName()
                                : null
                )
                .status(match.getStatus())
                .build();
    }
}