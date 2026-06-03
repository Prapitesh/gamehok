package org.ease.gamehok.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "leaderboards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Leaderboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    private Integer totalMatches;
    private Integer wins;
    private Integer losses;
    private Integer draws;
    private Integer totalKills;
    private Integer totalDeaths;
    private Double kdRatio;
    private Integer totalPoints;
    private Integer currentRank;
    private Integer previousRank;

    @Column(nullable = false)
    private String season;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Leaderboard(User user, String season) {
        this.user = user;
        this.season = season;
        this.totalMatches = 0;
        this.wins = 0;
        this.losses = 0;
        this.draws = 0;
        this.totalKills = 0;
        this.totalDeaths = 0;
        this.kdRatio = 0.0;
        this.totalPoints = 0;
        this.currentRank = 0;
        this.previousRank = 0;
    }
}
