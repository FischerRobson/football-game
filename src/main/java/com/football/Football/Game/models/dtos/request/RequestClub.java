package com.football.Football.Game.models.dtos.request;

import lombok.Data;

import java.util.UUID;

@Data
public class RequestClub {

    private String name;

    private UUID leagueId;

}
