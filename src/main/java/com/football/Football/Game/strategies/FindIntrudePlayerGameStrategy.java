package com.football.Football.Game.strategies;

import com.football.Football.Game.interfaces.GameStrategy;
import com.football.Football.Game.models.FindIntrudePlayerGame;
import com.football.Football.Game.models.Team;
import com.football.Football.Game.repositories.GuessTeamGameRepository;
import com.football.Football.Game.services.PlayerService;
import com.football.Football.Game.services.TeamService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class FindIntrudePlayerGameStrategy implements GameStrategy<Object, Object, Object> {

    private final TeamService teamService;
    private final PlayerService playerService;
    private final Logger logger = LogManager.getLogger(FindIntrudePlayerGameStrategy.class);

    public FindIntrudePlayerGameStrategy(TeamService teamService, PlayerService playerService) {
        this.teamService = teamService;
        this.playerService = playerService;
    }

    @Override
    public Object createGame() {
        Team randomTeam = this.teamService.findRandomTeam();
        List<String> playersNames = this.playerService.findPlayersNameByTeamSlug(randomTeam.getSlug());

        Collections.shuffle(playersNames);

        FindIntrudePlayerGame game = new FindIntrudePlayerGame();
        game.setPlayersNames(playersNames.subList(0, 4));

        return null;
    }

    @Override
    public Object play(Object o) {
        return null;
    }

    @Override
    public void finishGame(UUID gameId) {

    }
}
