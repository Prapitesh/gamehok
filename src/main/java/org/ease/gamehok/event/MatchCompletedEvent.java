package org.ease.gamehok.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchCompletedEvent {

    private Long matchId;

    private String winnerName;

    private String status;
}