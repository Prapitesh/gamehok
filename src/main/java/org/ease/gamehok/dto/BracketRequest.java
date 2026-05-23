package org.ease.gamehok.dto;

import lombok.Data;

import java.util.List;

@Data
public class BracketRequest {

    private List<Long> teamIds;
}