package org.ease.gamehok.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ease.gamehok.dto.MatchResponseDto;
import org.ease.gamehok.dto.MatchUpdateMessage;
import org.ease.gamehok.entity.Match;
import org.ease.gamehok.entity.Team;
import org.ease.gamehok.event.MatchCompletedEvent;
import org.ease.gamehok.exception.ResourceNotFoundException;
import org.ease.gamehok.kafka.MatchResultProducer;
import org.ease.gamehok.repository.MatchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchResultProducer matchResultProducer;

    private final SimpMessagingTemplate messagingTemplate;

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

        Match dbMatch =
                matchRepository.findById(matchId)
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

    public Match submitResult(
            Long matchId,
            Team winner
    ) {

        Match match =
                matchRepository.findById(matchId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Match not found"
                                )
                        );

        match.setWinner(winner);

        match.setStatus("COMPLETED");

        Match updatedMatch =
                matchRepository.save(match);

        // Kafka Event
        MatchCompletedEvent event =
                new MatchCompletedEvent(
                        updatedMatch.getId(),
                        winner.getTeamName(),
                        updatedMatch.getStatus()
                );

        matchResultProducer.sendMatchResult(
                "Match Completed -> MatchId: "
                        + updatedMatch.getId()
                        + ", Winner: "
                        + winner.getTeamName()
        );

        // WebSocket Message
        MatchUpdateMessage message =
                MatchUpdateMessage.builder()
                        .matchId(updatedMatch.getId())
                        .winner(
                                updatedMatch.getWinner()
                                        .getTeamName()
                        )
                        .status(updatedMatch.getStatus())
                        .roundNumber(
                                updatedMatch.getRoundNumber()
                        )
                        .build();

        messagingTemplate.convertAndSend(
                "/topic/matches",
                message
        );

        // Redis Cache Update
        try {

            redisMatchCacheService.saveMatch(updatedMatch);

        } catch (Exception e) {

            System.out.println("Redis unavailable");
        }

        return updatedMatch;
    }

    public Page<Match> getAllMatches(
            int page,
            int size,
            String sortBy,
            String status
    ) {

        Pageable pageable =
                PageRequest.of(
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

    public MatchResponseDto mapToDto(
            Match match
    ) {

        return MatchResponseDto.builder()
                .id(match.getId())
                .roundNumber(match.getRoundNumber())
                .team1(
                        match.getTeam1().getTeamName()
                )
                .team2(
                        match.getTeam2().getTeamName()
                )
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