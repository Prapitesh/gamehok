package org.ease.gamehok.repository;

import org.ease.gamehok.entity.Leaderboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaderboardRepository extends JpaRepository<Leaderboard, Long> {
    
    Optional<Leaderboard> findByUserIdAndSeason(Long userId, String season);
    
    Optional<Leaderboard> findByTeamIdAndSeason(Long teamId, String season);
    
    List<Leaderboard> findBySeason(String season);
    
    @Query("SELECT l FROM Leaderboard l WHERE l.season = :season ORDER BY l.totalPoints DESC")
    List<Leaderboard> findBySeasonOrderByTotalPointsDesc(@Param("season") String season);
    
    @Query("SELECT l FROM Leaderboard l WHERE l.season = :season ORDER BY l.currentRank ASC")
    List<Leaderboard> findBySeasonOrderByCurrentRankAsc(@Param("season") String season);
    
    @Query("SELECT l FROM Leaderboard l WHERE l.season = :season ORDER BY l.wins DESC")
    List<Leaderboard> findBySeasonOrderByWinsDesc(@Param("season") String season);
    
    @Query("SELECT l FROM Leaderboard l WHERE l.season = :season ORDER BY l.kdRatio DESC")
    List<Leaderboard> findBySeasonOrderByKdRatioDesc(@Param("season") String season);
    
    @Query("SELECT COUNT(l) FROM Leaderboard l WHERE l.season = :season")
    Long countBySeason(@Param("season") String season);
}
