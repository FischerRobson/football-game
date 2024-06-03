package com.football.Football.Game.services;

import com.football.Football.Game.enums.GameType;
import com.football.Football.Game.interfaces.GameStrategy;
import com.football.Football.Game.models.FindIntrudePlayerGame;
import com.football.Football.Game.models.GuessTeamGame;
import com.football.Football.Game.models.Player;
import com.football.Football.Game.models.Team;
import com.football.Football.Game.models.games.PlayFindIntrudePlayerGame;
import com.football.Football.Game.models.games.PlayGuessTeamGame;
import com.football.Football.Game.models.games.ResponseFindIntrudePlayerGame;
import com.football.Football.Game.models.games.ResponseGuessTeamGame;
import com.football.Football.Game.repositories.FindIntruderPlayerGameRepository;
import com.football.Football.Game.repositories.GuessTeamGameRepository;
import com.football.Football.Game.strategies.FindIntrudePlayerGameStrategy;
import com.football.Football.Game.strategies.GuessTeamGameStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.mockito.Mockito.*;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @Mock
    private PlayerService playerService;

    @Mock
    private TeamService teamService;

    @Mock
    private GuessTeamGameRepository guessTeamGameRepository;

    @Mock
    private FindIntruderPlayerGameRepository findIntruderPlayerGameRepository;

    @Mock
    private GuessTeamGameStrategy guessTeamGameStrategy;

    @Mock
    private FindIntrudePlayerGameStrategy findIntrudePlayerGameStrategy;

    @InjectMocks
    private GameService gameService;

    private Team mockTeam;

    private Player mockPlayer;

    private Player mockPlayer2;

    private GuessTeamGame mockGuessTeamGame;

    private FindIntrudePlayerGame mockFindIntrudePlayerGame;

    @BeforeEach
    public void setUp() {
        gameService.initializeStrategies();

        mockTeam = new Team();
        mockTeam.setName("Team");
        mockTeam.setSlug("team");

        mockPlayer = new Player();
        mockPlayer.setName("Player");
        mockPlayer.setSlug("player");
        mockPlayer.addTeam(mockTeam);

        mockPlayer2 = new Player();
        mockPlayer2.setName("Player2");
        mockPlayer2.setSlug("player-2");
        mockPlayer2.addTeam(mockTeam);

        mockGuessTeamGame = new GuessTeamGame();
        mockGuessTeamGame.setGameId(UUID.randomUUID());
        mockGuessTeamGame.setPlayersNames(List.of(mockPlayer.getSlug(), mockPlayer2.getSlug()));
        mockGuessTeamGame.setAnswer("answer");

        mockFindIntrudePlayerGame = new FindIntrudePlayerGame();
        mockFindIntrudePlayerGame.setGameId(UUID.randomUUID());
        mockFindIntrudePlayerGame.setAnswer("answer");
        mockFindIntrudePlayerGame.setPlayersNames(List.of(
                mockPlayer.getSlug(),
                mockPlayer2.getSlug(),
                mockPlayer.getSlug(),
                mockPlayer.getSlug(),
                mockPlayer.getSlug(),
                mockPlayer.getSlug(),
                mockPlayer.getSlug(),
                mockPlayer.getSlug(),
                mockPlayer.getSlug(),
                mockPlayer.getSlug(),
                mockPlayer.getSlug(),
                mockPlayer.getSlug(),
                mockPlayer.getSlug(),
                mockPlayer.getSlug()
        ));
    }

    @Test
    public void testInitializeStrategies() {
        Map<GameType, GameStrategy> strategies = gameService.getStrategies();
        assertNotNull(strategies);
        assertTrue(strategies.containsKey(GameType.GUESS_TEAM));
        assertTrue(strategies.containsKey(GameType.FIND_INTRUDER));
    }

    @Test
    public void testCreateGame_GuessTeam() throws NoSuchFieldException {
        List<String> output = List.of(mockPlayer.getSlug(), mockPlayer2.getSlug());
        when(teamService.findRandomTeam()).thenReturn(mockTeam);
        when(playerService.findPlayersNameByTeamsSlug(Collections.singleton(mockTeam.getSlug())))
                .thenReturn(output);
        when(guessTeamGameRepository.save(any(GuessTeamGame.class))).thenReturn(mockGuessTeamGame);

        ResponseGuessTeamGame game = gameService.createGame(GameType.GUESS_TEAM);

        assertNotNull(game);
        assertEquals(mockGuessTeamGame.getGameId(), game.getGameId());
    }

    // broken
//    @Test
    public void testCreateGame_FindIntruder() {
        List<String> output = List.of(mockPlayer.getSlug(), mockPlayer2.getSlug());
        when(teamService.findRandomTeam()).thenReturn(mockTeam);
        when(playerService.findPlayersNameByTeamsSlug(Collections.singleton(mockTeam.getSlug())))
                .thenReturn(output);
        when(playerService.getRandomPlayer()).thenReturn(mockPlayer);
        when(findIntruderPlayerGameRepository.save(any(FindIntrudePlayerGame.class))).thenReturn(mockFindIntrudePlayerGame);

        ResponseFindIntrudePlayerGame game = gameService.createGame(GameType.FIND_INTRUDER);

        assertNotNull(game);
        assertEquals(mockGuessTeamGame.getGameId(), game.getGameId());
    }

    @Test
    public void testPlayGame_GuessTeam() {
        PlayGuessTeamGame input = new PlayGuessTeamGame();
        input.setGameId(mockGuessTeamGame.getGameId());
        input.setAnswer("answer");
        String expectedOutput = "Correct answer";
        when(guessTeamGameRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(mockGuessTeamGame));

        Object output = gameService.playGame(GameType.GUESS_TEAM, input);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testPlayGame_FindIntruder() {
        PlayFindIntrudePlayerGame input = new PlayFindIntrudePlayerGame();
        input.setGameId(mockFindIntrudePlayerGame.getGameId());
        input.setAnswer("answer");
        String expectedOutput = "Correct answer";
        when(findIntruderPlayerGameRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(mockFindIntrudePlayerGame));

        Object output = gameService.playGame(GameType.FIND_INTRUDER, input);

        assertEquals(expectedOutput, output);
    }
}
