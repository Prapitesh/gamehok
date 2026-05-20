package org.ease.gamehok.service;


import lombok.extern.slf4j.Slf4j;
import org.ease.gamehok.entity.Match;
import org.ease.gamehok.entity.Team;
import org.ease.gamehok.exception.ResourceNotFoundException;
import org.ease.gamehok.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}