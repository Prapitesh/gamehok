package org.ease.gamehok.controller;

import lombok.RequiredArgsConstructor;
import org.ease.gamehok.entity.Leaderboard;
import org.ease.gamehok.service.LeaderboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboards")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @PostMapping
    public ResponseEntity<Leaderboard> createLeaderboardEntry(
            @RequestParam Long userId,
            @RequestParam(required = false) Long teamId,
            @RequestParam String season
    ) {
        Leaderboard leaderboard = leaderboardService.createLeaderboardEntry(userId, teamId, season);
        return ResponseEntity.ok(leaderboard);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Leaderboard> getLeaderboardByUser(
            @PathVariable Long userId,
            @RequestParam String season
    ) {
        Leaderboard leaderboard = leaderboardService.getLeaderboardByUserAndSeason(userId, season);
        return ResponseEntity.ok(leaderboard);
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<Leaderboard> getLeaderboardByTeam(
            @PathVariable Long teamId,
            @RequestParam String season
    ) {
        Leaderboard leaderboard = leaderboardService.getLeaderboardByTeamAndSeason(teamId, season);
        return ResponseEntity.ok(leaderboard);
    }

    @GetMapping("/season/{season}")
    public ResponseEntity<List<Leaderboard>> getLeaderboardBySeason(@PathVariable String season) {
        List<Leaderboard> leaderboards = leaderboardService.getLeaderboardBySeason(season);
        return ResponseEntity.ok(leaderboards);
    }

    @GetMapping("/season/{season}/rank")
    public ResponseEntity<List<Leaderboard>> getLeaderboardBySeasonOrderedByRank(@PathVariable String season) {
        List<Leaderboard> leaderboards = leaderboardService.getLeaderboardBySeasonOrderedByRank(season);
        return ResponseEntity.ok(leaderboards);
    }

    @GetMapping("/season/{season}/wins")
    public ResponseEntity<List<Leaderboard>> getLeaderboardBySeasonOrderedByWins(@PathVariable String season) {
        List<Leaderboard> leaderboards = leaderboardService.getLeaderboardBySeasonOrderedByWins(season);
        return ResponseEntity.ok(leaderboards);
    }

    @GetMapping("/season/{season}/kd")
    public ResponseEntity<List<Leaderboard>> getLeaderboardBySeasonOrderedByKdRatio(@PathVariable String season) {
        List<Leaderboard> leaderboards = leaderboardService.getLeaderboardBySeasonOrderedByKdRatio(season);
        return ResponseEntity.ok(leaderboards);
    }

    @PostMapping("/update")
    public ResponseEntity<Leaderboard> updateLeaderboardAfterMatch(
            @RequestParam Long userId,
            @RequestParam(required = false) Long teamId,
            @RequestParam String season,
            @RequestParam Integer kills,
            @RequestParam Integer deaths,
            @RequestParam Integer points,
            @RequestParam boolean isWin
    ) {
        Leaderboard leaderboard = leaderboardService.updateLeaderboardAfterMatch(
                userId, teamId, season, kills, deaths, points, isWin
        );
        return ResponseEntity.ok(leaderboard);
    }

    @PostMapping("/recalculate/{season}")
    public ResponseEntity<Void> recalculateRanks(@PathVariable String season) {
        leaderboardService.recalculateRanks(season);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Leaderboard> getLeaderboardById(@PathVariable Long id) {
        Leaderboard leaderboard = leaderboardService.getLeaderboardById(id);
        return ResponseEntity.ok(leaderboard);
    }

    @GetMapping
    public ResponseEntity<List<Leaderboard>> getAllLeaderboards() {
        List<Leaderboard> leaderboards = leaderboardService.getAllLeaderboards();
        return ResponseEntity.ok(leaderboards);
    }

    @GetMapping("/count/{season}")
    public ResponseEntity<Long> getLeaderboardCount(@PathVariable String season) {
        Long count = leaderboardService.getLeaderboardCount(season);
        return ResponseEntity.ok(count);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeaderboard(@PathVariable Long id) {
        leaderboardService.deleteLeaderboard(id);
        return ResponseEntity.noContent().build();
    }
}
