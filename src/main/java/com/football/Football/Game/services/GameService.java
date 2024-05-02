package com.football.Football.Game.services;

import com.football.Football.Game.models.GuessTeamGame;
import com.football.Football.Game.models.Team;
import com.football.Football.Game.models.dtos.response.ResponseGuessTeamGame;
import com.football.Football.Game.repositories.GuessTeamGameRepository;
import com.football.Football.Game.utils.Randomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.*;

@Service
public class GameService {

    @Autowired
    GuessTeamGameRepository guessTeamGameRepository;

    @Autowired
    PlayerService playerService;

    @Autowired
    TeamService teamService;

    private static final Logger logger = LogManager.getLogger(GameService.class);

    public ResponseGuessTeamGame createGuessTheTeamGame() {
        Team team = this.teamService.findRandomTeam();

        Set<String> teams = new HashSet<>();
        teams.add(team.getSlug());

        List<String> playersNames = this.playerService.findPlayersNameByTeamsSlug(teams);

        // repeat until find a valid game
        if (playersNames.size() < 2) {
            logger.warn("Please add more players to: " + team.getSlug());
            return this.createGuessTheTeamGame();
        }

        List<String> selectedPlayers = new ArrayList<>();
        List<Integer> indexes = Randomizer.generateRandomIndexes(playersNames.size());
        selectedPlayers.add(playersNames.get(indexes.get(0)));
        selectedPlayers.add(playersNames.get(indexes.get(1)));

        GuessTeamGame game = new GuessTeamGame();
        game.setAnswer(team.getSlug());
        game.setPlayersNames(selectedPlayers);

        GuessTeamGame savedGame = this.guessTeamGameRepository.save(game);
        logger.info("Answer for game: " + savedGame.getAnswer());
        return this.createResponseFromGuessTeamGame(savedGame);
    }

    private ResponseGuessTeamGame createResponseFromGuessTeamGame(GuessTeamGame game) {
        ResponseGuessTeamGame response = new ResponseGuessTeamGame();
        response.setGameId(game.getGameId());
        response.setPlayersNames(game.getPlayersNames());
        return response;
    }

}
