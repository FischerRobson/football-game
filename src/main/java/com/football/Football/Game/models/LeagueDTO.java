package com.football.Football.Game.models;

import lombok.Data;

import java.util.UUID;

@Data
public class LeagueDTO {

    private String name;

    private UUID countryId;

}
