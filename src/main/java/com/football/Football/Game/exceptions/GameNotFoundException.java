package com.football.Football.Game.exceptions;

public class GameNotFoundException extends RuntimeException {

    public GameNotFoundException() {
        super("Game not found");
    }

}
