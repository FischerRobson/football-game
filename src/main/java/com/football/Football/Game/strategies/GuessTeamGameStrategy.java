package com.football.Football.Game.strategies;

import com.football.Football.Game.exceptions.PlayerNotFoundException;
import com.football.Football.Game.exceptions.TeamNotFoundException;
import com.football.Football.Game.interfaces.GameStrategy;
import com.football.Football.Game.models.GuessTeamGame;
import com.football.Football.Game.models.Team;
import com.football.Football.Game.models.dtos.response.ResponseGuessTeamGame;
import com.football.Football.Game.repositories.GuessTeamGameRepository;
import com.football.Football.Game.services.PlayerService;
import com.football.Football.Game.services.TeamService;
import com.football.Football.Game.utils.Randomizer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GuessTeamGameStrategy implements GameStrategy<ResponseGuessTeamGame> {

    private final TeamService teamService;
    private final PlayerService playerService;
    private final Logger logger = LogManager.getLogger(GuessTeamGameStrategy.class);
    private final GuessTeamGameRepository guessTeamGameRepository;

    @Autowired
    public GuessTeamGameStrategy(TeamService teamService, PlayerService playerService, GuessTeamGameRepository guessTeamGameRepository) {
        this.teamService = teamService;
        this.playerService = playerService;
        this.guessTeamGameRepository = guessTeamGameRepository;
    }


    @Override
    public ResponseGuessTeamGame createGame() {
        Team team = null;
        try {
            team = this.teamService.findRandomTeam();
        } catch (Exception e) {
            throw new TeamNotFoundException();
        }

        Set<String> teams = new HashSet<>();
        teams.add(team.getSlug());

        List<String> playersNames = null;

        try {
            playersNames = this.playerService.findPlayersNameByTeamsSlug(teams);
        } catch (Exception e) {
            throw new PlayerNotFoundException();
        }

        // repeat until find a valid game
        if (playersNames.size() >= 2) {
            List<String> selectedPlayers = new ArrayList<>();
            List<Integer> indexes = Randomizer.generateRandomIndexes(playersNames.size());
            selectedPlayers.add(playersNames.get(indexes.get(0)));
            selectedPlayers.add(playersNames.get(indexes.get(1)));

            GuessTeamGame game = new GuessTeamGame();
            game.setAnswer(team.getSlug());
            game.setPlayersNames(selectedPlayers);

            GuessTeamGame savedGame = this.guessTeamGameRepository.save(game);
            logger.info("Answer for game " + savedGame.getGameId() +": " + savedGame.getAnswer());
            return this.createResponseFromGuessTeamGame(savedGame);
        }
        logger.warn("Please add more players to: " + team.getSlug());
        return this.createGame();
    }

    @Override
    public void finishGame() {

    }

    private ResponseGuessTeamGame createResponseFromGuessTeamGame(GuessTeamGame game) {
        ResponseGuessTeamGame response = new ResponseGuessTeamGame();
        response.setGameId(game.getGameId());
        response.setPlayersNames(game.getPlayersNames());
        return response;
    }
}
