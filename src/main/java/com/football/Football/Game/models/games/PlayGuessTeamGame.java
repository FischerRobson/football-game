package com.football.Football.Game.models.games;

import lombok.Data;

import java.util.UUID;

@Data
public class PlayGuessTeamGame {

    private UUID gameId;

    private String answer;

}
