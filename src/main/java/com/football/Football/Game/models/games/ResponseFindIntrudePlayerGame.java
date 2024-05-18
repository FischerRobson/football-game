package com.football.Football.Game.models.games;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ResponseFindIntrudePlayerGame {

    private UUID gameId;

    private List<String> playersNames;

}
