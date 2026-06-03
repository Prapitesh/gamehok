package org.ease.gamehok.service;

import lombok.RequiredArgsConstructor;
import org.ease.gamehok.entity.Leaderboard;
import org.ease.gamehok.entity.Match;
import org.ease.gamehok.entity.Team;
import org.ease.gamehok.entity.User;
import org.ease.gamehok.exception.ResourceNotFoundException;
import org.ease.gamehok.repository.LeaderboardRepository;
import org.ease.gamehok.repository.MatchRepository;
import org.ease.gamehok.repository.TeamRepository;
import org.ease.gamehok.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaderboardService {

    private final LeaderboardRepository leaderboardRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;

    public Leaderboard createLeaderboardEntry(Long userId, Long teamId, String season) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Team team = null;
        if (teamId != null) {
            team = teamRepository.findById(teamId)
                    .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + teamId));
        }

        Leaderboard leaderboard = new Leaderboard(user, season);
        leaderboard.setTeam(team);
        
        return leaderboardRepository.save(leaderboard);
    }

    public Leaderboard getLeaderboardByUserAndSeason(Long userId, String season) {
        return leaderboardRepository.findByUserIdAndSeason(userId, season)
                .orElseThrow(() -> new ResourceNotFoundException("Leaderboard entry not found for user: " + userId + " in season: " + season));
    }

    public Leaderboard getLeaderboardByTeamAndSeason(Long teamId, String season) {
        return leaderboardRepository.findByTeamIdAndSeason(teamId, season)
                .orElseThrow(() -> new ResourceNotFoundException("Leaderboard entry not found for team: " + teamId + " in season: " + season));
    }

    public List<Leaderboard> getLeaderboardBySeason(String season) {
        return leaderboardRepository.findBySeasonOrderByTotalPointsDesc(season);
    }

    public List<Leaderboard> getLeaderboardBySeasonOrderedByRank(String season) {
        return leaderboardRepository.findBySeasonOrderByCurrentRankAsc(season);
    }

    public List<Leaderboard> getLeaderboardBySeasonOrderedByWins(String season) {
        return leaderboardRepository.findBySeasonOrderByWinsDesc(season);
    }

    public List<Leaderboard> getLeaderboardBySeasonOrderedByKdRatio(String season) {
        return leaderboardRepository.findBySeasonOrderByKdRatioDesc(season);
    }

    @Transactional
    public Leaderboard updateLeaderboardAfterMatch(Long userId, Long teamId, String season, 
                                                     Integer kills, Integer deaths, Integer points, boolean isWin) {
        Leaderboard leaderboard = leaderboardRepository.findByUserIdAndSeason(userId, season)
                .orElseGet(() -> createLeaderboardEntry(userId, teamId, season));

        leaderboard.setTotalMatches(leaderboard.getTotalMatches() + 1);
        leaderboard.setTotalKills(leaderboard.getTotalKills() + kills);
        leaderboard.setTotalDeaths(leaderboard.getTotalDeaths() + deaths);
        leaderboard.setTotalPoints(leaderboard.getTotalPoints() + points);

        if (isWin) {
            leaderboard.setWins(leaderboard.getWins() + 1);
            leaderboard.setTotalPoints(leaderboard.getTotalPoints() + 100); // Bonus points for win
        } else {
            leaderboard.setLosses(leaderboard.getLosses() + 1);
        }

        // Calculate K/D ratio
        if (leaderboard.getTotalDeaths() > 0) {
            leaderboard.setKdRatio((double) leaderboard.getTotalKills() / leaderboard.getTotalDeaths());
        } else {
            leaderboard.setKdRatio((double) leaderboard.getTotalKills());
        }

        return leaderboardRepository.save(leaderboard);
    }

    @Transactional
    public void recalculateRanks(String season) {
        List<Leaderboard> leaderboards = leaderboardRepository.findBySeasonOrderByTotalPointsDesc(season);
        
        for (int i = 0; i < leaderboards.size(); i++) {
            Leaderboard leaderboard = leaderboards.get(i);
            leaderboard.setPreviousRank(leaderboard.getCurrentRank());
            leaderboard.setCurrentRank(i + 1);
            leaderboardRepository.save(leaderboard);
        }
    }

    public Leaderboard getLeaderboardById(Long id) {
        return leaderboardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leaderboard entry not found with id: " + id));
    }

    public List<Leaderboard> getAllLeaderboards() {
        return leaderboardRepository.findAll();
    }

    public void deleteLeaderboard(Long id) {
        Leaderboard leaderboard = leaderboardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leaderboard entry not found with id: " + id));
        leaderboardRepository.delete(leaderboard);
    }

    public Long getLeaderboardCount(String season) {
        return leaderboardRepository.countBySeason(season);
    }
}
