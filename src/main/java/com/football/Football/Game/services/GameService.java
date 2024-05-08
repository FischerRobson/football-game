package com.football.Football.Game.services;

import com.football.Football.Game.enums.GameType;
import com.football.Football.Game.interfaces.GameStrategy;
import com.football.Football.Game.repositories.GuessTeamGameRepository;
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

    private Map<GameType, GameStrategy> strategies;

    @PostConstruct
    private void initializeStrategies() {
        strategies = Map.of(
                GameType.GUESS_TEAM, new GuessTeamGameStrategy(teamService, playerService, guessTeamGameRepository)
        );
    }

    public Object createGame(GameType gameType) {
        return this.strategies.get(gameType).createGame();
    }

    public Object playGame(GameType gameType, Object input) {
        return this.strategies.get(gameType).play(input);
    }

}
