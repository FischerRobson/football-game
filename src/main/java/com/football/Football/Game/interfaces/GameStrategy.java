package com.football.Football.Game.interfaces;

public interface GameStrategy<TOutput> {
    TOutput createGame();

    void finishGame();
}
