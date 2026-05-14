package org.ease.gamehok.service;


import lombok.extern.slf4j.Slf4j;
import org.ease.gamehok.entity.Match;
import org.ease.gamehok.entity.Team;
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

        Match cachedMatch = redisMatchCacheService.getMatch(matchId);
        if (cachedMatch != null) {
            log.info("Fetched from Redis Cache");
            return cachedMatch;
        }

        Match dbMatch = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        redisMatchCacheService.saveMatch(dbMatch);

        log.info("Fetched from PostgreSQL");

        return dbMatch;
    }

    public Match submitResult(Long matchId, Team winner) {

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        match.setWinner(winner);
        match.setStatus("COMPLETED");

        Match updatedMatch = matchRepository.save(match);

        redisMatchCacheService.saveMatch(updatedMatch);

        return updatedMatch;
    }
}