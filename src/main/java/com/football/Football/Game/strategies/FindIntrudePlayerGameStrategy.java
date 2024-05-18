package com.football.Football.Game.strategies;

import com.football.Football.Game.exceptions.GameNotFoundException;
import com.football.Football.Game.interfaces.GameStrategy;
import com.football.Football.Game.models.FindIntrudePlayerGame;
import com.football.Football.Game.models.Player;
import com.football.Football.Game.models.Team;
import com.football.Football.Game.models.games.PlayFindIntrudePlayerGame;
import com.football.Football.Game.models.games.ResponseFindIntrudePlayerGame;
import com.football.Football.Game.repositories.FindIntruderPlayerGameRepository;
import com.football.Football.Game.services.PlayerService;
import com.football.Football.Game.services.TeamService;
import com.football.Football.Game.utils.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.*;

public class FindIntrudePlayerGameStrategy implements GameStrategy<ResponseFindIntrudePlayerGame,
        PlayFindIntrudePlayerGame, String> {

    private final TeamService teamService;
    private final PlayerService playerService;
    private final FindIntruderPlayerGameRepository findIntruderPlayerGameRepository;
    private final Logger logger = LogManager.getLogger(FindIntrudePlayerGameStrategy.class);

    public FindIntrudePlayerGameStrategy(TeamService teamService, PlayerService playerService,
                                         FindIntruderPlayerGameRepository findIntruderPlayerGameRepository) {
        this.teamService = teamService;
        this.playerService = playerService;
        this.findIntruderPlayerGameRepository = findIntruderPlayerGameRepository;
    }

    @Override
    public ResponseFindIntrudePlayerGame createGame() {
        Team randomTeam = this.teamService.findRandomTeam();

        while (this.teamService.countPlayerByTeam(randomTeam.getSlug()) < 10) {
            randomTeam = this.teamService.findRandomTeam();
        }

        FindIntrudePlayerGame game = new FindIntrudePlayerGame();

        List<String> playersNames = this.playerService.findPlayersNameByTeamSlug(randomTeam.getSlug());
        List<String> copy = new ArrayList<>(playersNames); // assure mutability

        try {
           Collections.shuffle(copy);
           copy = copy.subList(0, Math.min(copy.size(), 5));
           Player intrude = this.playerService.getRandomPlayer();
           game.setAnswer(intrude.getName());
           copy.add(intrude.getName());
           Collections.shuffle(copy);

           game.setPlayersNames(copy);

           FindIntrudePlayerGame savedGame = this.findIntruderPlayerGameRepository.save(game);
           return this.createResponseFromFindIntrudeGame(savedGame);
        } catch (Exception e) {
           logger.error("Failed to shuffle Intrude Game: " + e.getMessage());
           logger.error("Exception :", e);
        }

        return null;
    }

    @Override
    public String play(PlayFindIntrudePlayerGame playGame) {

        FindIntrudePlayerGame game = this.findIntruderPlayerGameRepository.findById(playGame.getGameId())
                .orElseThrow(GameNotFoundException::new);

        if (game.getAnswer().contains(playGame.getAnswer())) {
            this.finishGame(game.getGameId());
            return "Correct answer";
        }
        return "Wrong answer";
    }

    @Override
    public void finishGame(UUID gameId) {
        this.findIntruderPlayerGameRepository.deleteById(gameId);
    }

    @Override
    @Scheduled(fixedRate = Constants.TWENTY_MINUTES)
    public void deleteOldGames() {
        logger.warn("Deleting old games...");
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(20);
        this.findIntruderPlayerGameRepository.deleteByCreatedAtBefore(cutoff);
    }

    private ResponseFindIntrudePlayerGame createResponseFromFindIntrudeGame(FindIntrudePlayerGame game) {
        ResponseFindIntrudePlayerGame response = new ResponseFindIntrudePlayerGame();
        response.setGameId(game.getGameId());
        response.setPlayersNames(game.getPlayersNames());
        return response;
    }
}
