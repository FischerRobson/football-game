package com.football.Football.Game.exceptions;

public class TeamNotFoundException extends RuntimeException {

    public TeamNotFoundException() {
        super("Team not found");
    }

}
