package org.ease.gamehok.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTournamentRequest {

    @NotBlank(message = "Tournament name is required")
    private String name;

    @NotBlank(message = "Game name is required")
    private String game;

    @NotBlank(message = "Format type is required")
    private String formatType;

    @NotNull(message = "Prize pool is required")
    private Double prizePool;
}