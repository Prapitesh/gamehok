package org.ease.gamehok.service;

import org.ease.gamehok.entity.Match;
import org.ease.gamehok.entity.Team;
import org.ease.gamehok.exception.ResourceNotFoundException;
import org.ease.gamehok.kafka.MatchResultProducer;
import org.ease.gamehok.repository.MatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyString;
@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private RedisMatchCacheService redisMatchCacheService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private MatchResultProducer matchResultProducer;

    @InjectMocks
    private MatchService matchService;

    private Match match;

    @BeforeEach
    void setup() {

        Team team1 = Team.builder()
                .id(1L)
                .teamName("Alpha")
                .build();

        Team team2 = Team.builder()
                .id(2L)
                .teamName("Beta")
                .build();

        match = Match.builder()
                .id(1L)
                .team1(team1)
                .team2(team2)
                .status("PENDING")
                .build();
    }

    @Test
    void shouldReturnMatchSuccessfully() {

        when(matchRepository.findById(1L))
                .thenReturn(Optional.of(match));

        Match result =
                matchService.getMatchById(1L);

        assertNotNull(result);

        assertEquals(
                "PENDING",
                result.getStatus()
        );

        verify(matchRepository, times(1))
                .findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenMatchNotFound() {

        when(matchRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> matchService.getMatchById(1L)
        );
    }

    @Test
    void shouldSubmitMatchResult() {

        Team winner = Team.builder()
                .id(1L)
                .teamName("Alpha")
                .build();

        when(matchRepository.findById(1L))
                .thenReturn(Optional.of(match));

        when(matchRepository.save(any(Match.class)))
                .thenReturn(match);

        Match updated =
                matchService.submitResult(1L, winner);

        assertEquals(
                "COMPLETED",
                updated.getStatus()
        );

        assertEquals(
                "Alpha",
                updated.getWinner().getTeamName()
        );

        verify(matchResultProducer, times(1))
                .sendMatchResult(anyString());

        verify(messagingTemplate, times(1))
                .convertAndSend(
                        eq("/topic/matches"),
                        any(Object.class)
                );
    }
}