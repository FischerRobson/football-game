package com.football.Football.Game.exceptions;

public class LeagueAlreadyExistsException extends RuntimeException {

    public LeagueAlreadyExistsException() {
        super("League already exists");
    }

}
