package org.ease.gamehok.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TournamentResponse {

    private Long id;

    private String name;

    private String game;

    private String mode;

    private String status;

    private Double prizePool;

    private String bannerUrl;
}