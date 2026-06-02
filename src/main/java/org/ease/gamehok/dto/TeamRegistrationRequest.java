package org.ease.gamehok.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class TeamRegistrationRequest {

    @NotBlank(message = "Team name is required")
    private String teamName;

    private List<Long> playerIds;

    private String logoUrl;
}
