package com.football.Football.Game.services;

import com.football.Football.Game.exceptions.PlayerNotFoundException;
import com.football.Football.Game.exceptions.TeamNotFoundException;
import com.football.Football.Game.models.League;
import com.football.Football.Game.models.Player;
import com.football.Football.Game.models.Team;
import com.football.Football.Game.models.dtos.request.RequestPlayer;
import com.football.Football.Game.models.dtos.response.ResponsePlayer;
import com.football.Football.Game.repositories.PlayerRepository;
import com.football.Football.Game.repositories.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private PlayerService playerService;

    private RequestPlayer requestPlayer;
    private Player player;
    private Team team;

    @BeforeEach
    void setUp() {
        requestPlayer = new RequestPlayer();
        requestPlayer.setName("Test Player");
        requestPlayer.setTeamsSlugs(Arrays.asList("test-team"));

        team = new Team();
        team.setId(UUID.randomUUID());
        team.setLeague(new League());
        team.setName("Test Team");
        team.setSlug("test-team");

        player = new Player();
        player.setId(UUID.randomUUID());
        player.setName("Test Player");
        player.setSlug("test-player");
        player.setTeams(new HashSet<>(Collections.singletonList(team)));
    }

    @Test
    void testCreatePlayer_Success() {
        when(playerRepository.save(any(Player.class))).thenReturn(player);
        when(teamRepository.findBySlug(anyString())).thenReturn(Optional.of(team));

        ResponsePlayer response = playerService.createPlayer(requestPlayer);

        assertNotNull(response);
        assertEquals("Test Player", response.getName());
        verify(playerRepository, times(1)).save(any(Player.class));
    }

    @Test
    void testCreatePlayerFromName_PlayerExists() {
        when(playerRepository.findBySlug(anyString())).thenReturn(Optional.of(player));

        Player foundPlayer = playerService.createPlayerFromName("Test Player");

        assertNotNull(foundPlayer);
        assertEquals("Test Player", foundPlayer.getName());
        verify(playerRepository, never()).save(any(Player.class));
    }

    @Test
    void testCreatePlayerFromName_NewPlayer() {
        when(playerRepository.findBySlug(anyString())).thenReturn(Optional.empty());
        when(playerRepository.save(any(Player.class))).thenReturn(player);

        Player newPlayer = playerService.createPlayerFromName("Test Player");

        assertNotNull(newPlayer);
        assertEquals("Test Player", newPlayer.getName());
        verify(playerRepository, times(1)).save(any(Player.class));
    }

    @Test
    void testAddTeamToPlayer_Success() {
        when(playerRepository.findBySlug(anyString())).thenReturn(Optional.of(player));
        when(teamRepository.findBySlug(anyString())).thenReturn(Optional.of(team));

        playerService.addTeamToPlayer("test-player", "test-team");

        verify(playerRepository, times(1)).save(any(Player.class));
    }

    @Test
    void testAddTeamToPlayer_PlayerNotFound() {
        when(playerRepository.findBySlug(anyString())).thenReturn(Optional.empty());

        assertThrows(PlayerNotFoundException.class, () -> {
            playerService.addTeamToPlayer("invalid-player", "test-team");
        });

        verify(playerRepository, never()).save(any(Player.class));
    }

    @Test
    void testAddTeamToPlayer_TeamNotFound() {
        when(playerRepository.findBySlug(anyString())).thenReturn(Optional.of(player));
        when(teamRepository.findBySlug(anyString())).thenReturn(Optional.empty());

        assertThrows(TeamNotFoundException.class, () -> {
            playerService.addTeamToPlayer("test-player", "invalid-team");
        });

        verify(playerRepository, never()).save(any(Player.class));
    }

    @Test
    void testCheckIfPlayerHasTeams_Success() {
        when(playerRepository.findBySlug(anyString())).thenReturn(Optional.of(player));

        boolean result = playerService.checkIfPlayerHasTeams("test-player", Arrays.asList("test-team"));

        assertTrue(result);
    }

    @Test
    void testCheckIfPlayerHasTeams_PlayerNotFound() {
        when(playerRepository.findBySlug(anyString())).thenReturn(Optional.empty());

        assertThrows(PlayerNotFoundException.class, () -> {
            playerService.checkIfPlayerHasTeams("invalid-player", Arrays.asList("test-team"));
        });
    }

    @Test
    void testGetRandomPlayer_Success() {
        when(playerRepository.findRandomPlayer()).thenReturn(Optional.of(player));

        Player randomPlayer = playerService.getRandomPlayer();

        assertNotNull(randomPlayer);
        assertEquals("Test Player", randomPlayer.getName());
    }

    @Test
    void testGetRandomPlayer_NoPlayerFound() {
        when(playerRepository.findRandomPlayer()).thenReturn(Optional.empty());

        Player randomPlayer = playerService.getRandomPlayer();

        assertNull(randomPlayer);
    }

    @Test
    void testFindPlayersNameByTeamsSlug_Success() {
        Set<String> teamSlugs = new HashSet<>(Collections.singletonList("test-team"));
        when(playerRepository.findPlayersByTeamSlugs(anySet(), anyInt())).thenReturn(Collections.singletonList(player));

        List<String> playerNames = playerService.findPlayersNameByTeamsSlug(teamSlugs);

        assertNotNull(playerNames);
        assertFalse(playerNames.isEmpty());
        assertEquals("Test Player", playerNames.get(0));
    }

    @Test
    void testFindPlayersNameByTeamSlug_Success() {
        when(playerRepository.findPlayersByTeamSlugs(anySet(), anyInt())).thenReturn(Collections.singletonList(player));

        List<String> playerNames = playerService.findPlayersNameByTeamSlug("test-team");

        assertNotNull(playerNames);
        assertFalse(playerNames.isEmpty());
        assertEquals("Test Player", playerNames.get(0));
    }

    @Test
    void testAddTeamsToPlayer_Success() {
        Set<String> teamsSlugs = new HashSet<>(Arrays.asList("test-team"));
        when(playerRepository.findBySlug(anyString())).thenReturn(Optional.of(player));

        when(playerRepository.save(any(Player.class))).thenReturn(player);

        ResponsePlayer response = playerService.addTeamsToPlayer(teamsSlugs, "test-player");

        assertNotNull(response);
        assertEquals("Test Player", response.getName());
        verify(playerRepository, times(1)).save(any(Player.class));
    }

    @Test
    void testAddTeamsToPlayer_PlayerNotFound() {
        Set<String> teamsSlugs = new HashSet<>(Arrays.asList("test-team"));
        when(playerRepository.findBySlug(anyString())).thenReturn(Optional.empty());

        assertThrows(PlayerNotFoundException.class, () -> {
            playerService.addTeamsToPlayer(teamsSlugs, "invalid-player");
        });

        verify(playerRepository, never()).save(any(Player.class));
    }

    @Test
    void testAddTeamsToPlayer_TeamNotFound() {
        Set<String> teamsSlugs = new HashSet<>(Arrays.asList("invalid-team"));
        when(playerRepository.findBySlug(anyString())).thenReturn(Optional.of(player));
        when(teamRepository.findBySlug(anyString())).thenReturn(Optional.empty());

        assertThrows(TeamNotFoundException.class, () -> {
            playerService.addTeamsToPlayer(teamsSlugs, "test-player");
        });

        verify(playerRepository, never()).save(any(Player.class));
    }
}