package org.ease.gamehok.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisputeRequest {
    private Long matchId;
    private Long teamId;
    private String reason;
}
