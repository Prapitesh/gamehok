package org.ease.gamehok.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchUpdateMessage {

    private Long matchId;

    private String winner;

    private String status;

    private Integer roundNumber;
}