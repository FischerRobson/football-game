package com.football.Football.Game.exceptions;

public class EnvironmentException extends RuntimeException {
    public EnvironmentException() {
        super("Failed on run in this environment");
    }
}
