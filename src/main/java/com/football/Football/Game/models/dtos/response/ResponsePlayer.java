package com.football.Football.Game.models.dtos.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ResponsePlayer {

    private UUID id;

    private String name;

    private String slug;

    private List<ResponseTeam> teams;

}
