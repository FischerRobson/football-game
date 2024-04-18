package com.football.Football.Game.exceptions;

public class InvalidTeamRequestException extends RuntimeException {

    public InvalidTeamRequestException() {
        super("Invalid Team Request");
    }

}
