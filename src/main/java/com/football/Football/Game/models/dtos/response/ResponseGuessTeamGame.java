package com.football.Football.Game.models.dtos.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ResponseGuessTeamGame {

    private UUID gameId;

    private List<String> playersNames;

}
