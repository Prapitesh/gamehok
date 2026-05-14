package org.ease.gamehok.service;


import org.ease.gamehok.entity.Match;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisMatchCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void saveMatch(Match match) {

        String key = "match:" + match.getId();

        redisTemplate.opsForValue().set(
                key,
                match,
                Duration.ofMinutes(30)
        );
    }

    public Match getMatch(Long matchId) {

        String key = "match:" + matchId;

        Object cachedMatch = redisTemplate.opsForValue().get(key);

        if (cachedMatch == null) {
            return null;
        }

        return (Match) cachedMatch;
    }

    public void deleteMatch(Long matchId) {

        String key = "match:" + matchId;

        redisTemplate.delete(key);
    }
}
