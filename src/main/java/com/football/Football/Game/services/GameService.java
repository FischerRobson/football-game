package com.football.Football.Game.services;

import com.football.Football.Game.enums.GameType;
import com.football.Football.Game.interfaces.GameStrategy;
import com.football.Football.Game.repositories.FindIntruderPlayerGameRepository;
import com.football.Football.Game.repositories.GuessTeamGameRepository;
import com.football.Football.Game.strategies.FindIntrudePlayerGameStrategy;
import com.football.Football.Game.strategies.GuessTeamGameStrategy;
import jakarta.annotation.PostConstruct;
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

    private Map<GameType, GameStrategy> strategies;

    @PostConstruct
    private void initializeStrategies() {
        strategies = Map.of(
                GameType.GUESS_TEAM, new GuessTeamGameStrategy(teamService, playerService, guessTeamGameRepository),
                GameType.FIND_INTRUDER, new FindIntrudePlayerGameStrategy(teamService, playerService,
                        findIntruderPlayerGameRepository)
        );
    }

    public Object createGame(GameType gameType) {
        return this.strategies.get(gameType).createGame();
    }

    public Object playGame(GameType gameType, Object input) {
        GameStrategy strategy = this.strategies.get(gameType);
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported game type: " + gameType);
        }
        return strategy.play(input);
    }

}
