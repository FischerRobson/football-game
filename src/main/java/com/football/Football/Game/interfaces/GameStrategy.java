package com.football.Football.Game.interfaces;

import java.util.UUID;

public interface GameStrategy<TGame, TPlayInput, TPlayOutput> {
    TGame createGame();

    TPlayOutput play(TPlayInput tPlayInput);

    void finishGame(UUID gameId);

    void deleteOldGames();
}
