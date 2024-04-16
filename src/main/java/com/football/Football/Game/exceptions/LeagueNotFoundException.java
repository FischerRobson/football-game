package com.football.Football.Game.exceptions;

public class LeagueNotFoundException extends RuntimeException {

    public LeagueNotFoundException() {
        super("League not found");
    }

}
