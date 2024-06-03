package com.football.Football.Game.services;

import com.football.Football.Game.enums.GameType;
import com.football.Football.Game.interfaces.GameStrategy;
import com.football.Football.Game.repositories.FindIntruderPlayerGameRepository;
import com.football.Football.Game.repositories.GuessTeamGameRepository;
import com.football.Football.Game.strategies.FindIntrudePlayerGameStrategy;
import com.football.Football.Game.strategies.GuessTeamGameStrategy;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class GameService {

    @Autowired
    PlayerService playerService;

    @Autowired
    TeamService teamService;

    @Autowired
    GuessTeamGameRepository guessTeamGameRepository;

    @Autowired
    FindIntruderPlayerGameRepository findIntruderPlayerGameRepository;

    @Getter
    private Map<GameType, GameStrategy> strategies;

    @PostConstruct
    void initializeStrategies() {
        strategies = Map.of(
                GameType.GUESS_TEAM, new GuessTeamGameStrategy(teamService, playerService, guessTeamGameRepository),
                GameType.FIND_INTRUDER, new FindIntrudePlayerGameStrategy(teamService, playerService,
                        findIntruderPlayerGameRepository)
        );
    }

    public <TCreateOutput> TCreateOutput createGame(GameType gameType) {

        @SuppressWarnings("unchecked")
        GameStrategy<TCreateOutput, ?, ?> strategy = (GameStrategy<TCreateOutput, ?, ?>)
                this.strategies.get(gameType);

        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported game type: " + gameType);
        }
        return strategy.createGame();
    }

    public <TGame, TPlayInput, TPlayOutput> TPlayOutput playGame(GameType gameType, TPlayInput input) {

        @SuppressWarnings("unchecked")
        GameStrategy<TGame, TPlayInput, TPlayOutput> strategy = (GameStrategy<TGame, TPlayInput, TPlayOutput>)
                this.strategies.get(gameType);

        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported game type: " + gameType);
        }
        return strategy.play(input);
    }

}
