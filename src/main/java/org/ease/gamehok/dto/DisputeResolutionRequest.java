package org.ease.gamehok.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisputeResolutionRequest {
    private String status;
    private String adminNotes;
}
