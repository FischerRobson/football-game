package com.football.Football.Game.exceptions;

public class CountryNotFoundException extends RuntimeException{

    public CountryNotFoundException() {
        super("Country not found");
    }

}
