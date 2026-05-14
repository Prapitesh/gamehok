package org.ease.gamehok.dto;

import lombok.Data;

import java.util.List;

@Data
public class TeamRegistrationRequest {

    private String teamName;
    private List<Long> playerIds;
}
