package com.football.Football.Game.exceptions;

public class TeamAlreadyExistsException extends RuntimeException {

    public TeamAlreadyExistsException() {
        super("Team already exists");
    }

}
