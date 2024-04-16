package com.football.Football.Game.models.dtos.response;

import lombok.Data;

import java.util.UUID;

@Data
public class ResponseTeam {

    private UUID id;

    private String name;

    private String leagueName;

    private String slug;
}
