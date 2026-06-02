package org.ease.gamehok.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisputeResponse {
    private Long id;
    private Long matchId;
    private String matchName;
    private Long teamId;
    private String teamName;
    private Long userId;
    private String userEmail;
    private String reason;
    private String status;
    private String adminNotes;
    private Long resolvedBy;
    private String resolvedByEmail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
