package org.ease.gamehok.dto;

import lombok.Data;

@Data
public class TournamentRequest {

    private String name;

    private String game;

    private String mode;

    private Double prizePool;

    private String bannerUrl;
}