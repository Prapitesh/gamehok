package org.ease.gamehok.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchResponseDto {

    private Long id;

    private Integer roundNumber;

    private String team1;

    private String team2;

    private String winner;

    private String status;
}