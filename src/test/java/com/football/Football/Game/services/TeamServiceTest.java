package com.football.Football.Game.services;

import com.football.Football.Game.exceptions.LeagueNotFoundException;
import com.football.Football.Game.exceptions.TeamAlreadyExistsException;
import com.football.Football.Game.exceptions.TeamNotFoundException;
import com.football.Football.Game.models.League;
import com.football.Football.Game.models.Team;
import com.football.Football.Game.models.dtos.request.RequestManyTeamsByLeague;
import com.football.Football.Game.models.dtos.request.RequestTeam;
import com.football.Football.Game.models.dtos.response.ResponseTeam;
import com.football.Football.Game.models.dtos.response.ResponseTeamByLeague;
import com.football.Football.Game.repositories.LeagueRepository;
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
public class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private LeagueRepository leagueRepository;

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private TeamService teamService;

    private RequestTeam requestTeam;
    private Team team;
    private League league;

    @BeforeEach
    void setUp() {
        requestTeam = new RequestTeam();
        requestTeam.setName("Test Team");
        requestTeam.setLeagueSlug("test-league");

        league = new League();
        league.setId(UUID.fromString("f1ef839b-e088-4e48-82a9-07116242b122"));
        league.setName("Test League");
        league.setSlug("test-league");

        team = new Team();
        team.setId(UUID.fromString("16fc2cf3-b0c4-4fd8-b43e-138685dd38e2"));
        team.setName("Test Team");
        team.setSlug("test-team");
        team.setLeague(league);
    }

    @Test
    public void testCreateTeam_Success() {
        when(teamRepository.findBySlug(anyString())).thenReturn(Optional.empty());
        when(leagueRepository.findBySlug(anyString())).thenReturn(Optional.of(league));
        when(teamRepository.save(any(Team.class))).thenReturn(team);

        ResponseTeam savedTeam = this.teamService.createTeam(requestTeam);

        assertNotNull(savedTeam);
        assertEquals("Test Team", savedTeam.getName());
        verify(teamRepository, times(1)).save(any(Team.class));
    }

    @Test
    void testCreateTeam_TeamAlreadyExists() {
        when(teamRepository.findBySlug(anyString())).thenReturn(Optional.of(team));

        assertThrows(TeamAlreadyExistsException.class, () -> {
            teamService.createTeam(requestTeam);
        });

        verify(teamRepository, never()).save(any(Team.class));
    }

    @Test
    void testFindTeamsByLeague_Success() {
        when(leagueRepository.findBySlug(anyString())).thenReturn(Optional.of(league));
        when(teamRepository.findAllByLeagueId(any(UUID.class))).thenReturn(Optional.of(Collections.singletonList(team)));

        List<ResponseTeamByLeague> response = teamService.findTeamsByLeague("test-league");

        assertNotNull(response);
        assertFalse(response.isEmpty());
        verify(teamRepository, times(1)).findAllByLeagueId(any(UUID.class));
    }

    @Test
    void testFindTeamsByLeague_LeagueNotFound() {
        when(leagueRepository.findBySlug(anyString())).thenReturn(Optional.empty());

        assertThrows(LeagueNotFoundException.class, () -> {
            teamService.findTeamsByLeague("invalid-league");
        });

        verify(teamRepository, never()).findAllByLeagueId(any(UUID.class));
    }

    @Test
    void testFindTeamsByLeague_TeamNotFound() {
        when(leagueRepository.findBySlug(anyString())).thenReturn(Optional.of(league));
        when(teamRepository.findAllByLeagueId(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(TeamNotFoundException.class, () -> {
            teamService.findTeamsByLeague("test-league");
        });

        verify(teamRepository, times(1)).findAllByLeagueId(any(UUID.class));
    }

    @Test
    void testCountPlayerByTeam_TeamNotFound() {
        when(teamRepository.findBySlug(anyString())).thenReturn(Optional.empty());

        assertThrows(TeamNotFoundException.class, () -> {
            teamService.countPlayerByTeam("invalid-team");
        });

        verify(teamRepository, never()).countPlayersByTeamSlug(anyString());
    }

    @Test
    void testCountPlayerByTeam_Success() {
        when(teamRepository.findBySlug(anyString())).thenReturn(Optional.of(team));
        when(teamRepository.countPlayersByTeamSlug(anyString())).thenReturn(5);

        int count = teamService.countPlayerByTeam("test-team");

        assertEquals(5, count);
        verify(teamRepository, times(1)).countPlayersByTeamSlug(anyString());
    }

    @Test
    void testCreateManyTeamsByLeague_Success() {
        RequestManyTeamsByLeague request = new RequestManyTeamsByLeague();
        request.setLeagueSlug("test-league");
        request.setTeamsNames(new HashSet<>(Arrays.asList("Team 1", "Team 2")));

        when(leagueRepository.findBySlug(anyString())).thenReturn(Optional.of(league));
        when(teamRepository.findBySlug(anyString())).thenReturn(Optional.empty());
        when(teamRepository.save(any(Team.class))).thenReturn(team);

        List<ResponseTeamByLeague> response = teamService.createManyTeamsByLeague(request);

        assertNotNull(response);
        assertFalse(response.isEmpty());
        verify(teamRepository, times(2)).save(any(Team.class));
    }

    @Test
    void testCreateManyTeamsByLeague_LeagueNotFound() {
        RequestManyTeamsByLeague request = new RequestManyTeamsByLeague();
        request.setLeagueSlug("invalid-league");
        request.setTeamsNames(new HashSet<>(Arrays.asList("Team 1", "Team 2")));

        when(leagueRepository.findBySlug(anyString())).thenReturn(Optional.empty());

        assertThrows(LeagueNotFoundException.class, () -> {
            teamService.createManyTeamsByLeague(request);
        });

        verify(teamRepository, never()).save(any(Team.class));
    }
}
