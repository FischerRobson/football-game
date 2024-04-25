package com.football.Football.Game.models.dtos.request;

import lombok.Data;

import java.util.Set;

@Data
public class RequestManyTeamsByLeague {

    private String leagueSlug;

    Set<String> teamsNames;

}
