package com.football.Football.Game.models.dtos.request;

import lombok.Data;

import java.util.List;

@Data
public class RequestAddPlayersToTeam {

    private List<String> playersSlugs;

}
