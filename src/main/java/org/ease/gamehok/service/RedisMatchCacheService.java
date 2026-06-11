package org.ease.gamehok.service;


import lombok.RequiredArgsConstructor;
import org.ease.gamehok.entity.Match;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.redis.enabled", havingValue = "true", matchIfMissing = false)
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
