package com.football.Football.Game.models.dtos.request;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class RequestPlayer {

    private String name;

    private List<UUID> teamsId;

    private List<String> teamsSlugs;

}
