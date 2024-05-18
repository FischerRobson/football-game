package com.football.Football.Game.strategies;

import com.football.Football.Game.exceptions.GameNotFoundException;
import com.football.Football.Game.exceptions.PlayerNotFoundException;
import com.football.Football.Game.exceptions.TeamNotFoundException;
import com.football.Football.Game.interfaces.GameStrategy;
import com.football.Football.Game.models.GuessTeamGame;
import com.football.Football.Game.models.Team;
import com.football.Football.Game.models.games.ResponseGuessTeamGame;
import com.football.Football.Game.models.games.PlayGuessTeamGame;
import com.football.Football.Game.repositories.GuessTeamGameRepository;
import com.football.Football.Game.services.PlayerService;
import com.football.Football.Game.services.TeamService;
import com.football.Football.Game.utils.Constants;
import com.football.Football.Game.utils.Randomizer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.*;

public class GuessTeamGameStrategy implements GameStrategy<ResponseGuessTeamGame, PlayGuessTeamGame, String> {

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
            return this.createResponseFromGuessTeamGame(savedGame);
        }
        logger.warn("Please add more players to: " + team.getSlug());
        return this.createGame();
    }

    @Override
    public String play(PlayGuessTeamGame play) {
        GuessTeamGame game = this.guessTeamGameRepository.findById(play.getGameId())
                .orElseThrow(GameNotFoundException::new);

        if (game.getAnswer().toLowerCase().contains(play.getAnswer().toLowerCase())) {
            this.finishGame(play.getGameId());
            return "Correct answer";
        }
        return "Wrong answer";
    }

    @Override
    @Scheduled(fixedRate = Constants.TWENTY_MINUTES)
    public void deleteOldGames() {
        logger.warn("Deleting old games...");
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(20);
        this.guessTeamGameRepository.deleteByCreatedAtBefore(cutoff);
    }

    @Override
    public void finishGame(UUID gameId) {
        this.guessTeamGameRepository.deleteById(gameId);
    }

    private ResponseGuessTeamGame createResponseFromGuessTeamGame(GuessTeamGame game) {
        ResponseGuessTeamGame response = new ResponseGuessTeamGame();
        response.setGameId(game.getGameId());
        response.setPlayersNames(game.getPlayersNames());
        return response;
    }
}
