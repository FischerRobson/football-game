package com.football.Football.Game.services;

import com.football.Football.Game.exceptions.InvalidTeamRequestException;
import com.football.Football.Game.exceptions.LeagueNotFoundException;
import com.football.Football.Game.exceptions.TeamAlreadyExistsException;
import com.football.Football.Game.exceptions.TeamNotFoundException;
import com.football.Football.Game.models.League;
import com.football.Football.Game.models.Player;
import com.football.Football.Game.models.Team;
import com.football.Football.Game.models.dtos.request.RequestAddPlayersToTeam;
import com.football.Football.Game.models.dtos.request.RequestManyTeamsByLeague;
import com.football.Football.Game.models.dtos.request.RequestTeam;
import com.football.Football.Game.models.dtos.response.ResponseTeam;
import com.football.Football.Game.models.dtos.response.ResponseTeamByLeague;
import com.football.Football.Game.repositories.LeagueRepository;
import com.football.Football.Game.repositories.PlayerRepository;
import com.football.Football.Game.repositories.TeamRepository;
import com.football.Football.Game.utils.SlugGenerator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TeamService {

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    LeagueRepository leagueRepository;

    @Autowired
    PlayerService playerService;

    public ResponseTeam createTeam(RequestTeam requestTeam) {

        String slug = SlugGenerator.generateSlug(requestTeam.getName());

        Optional<Team> teamAlreadyExists = this.teamRepository.findBySlug(slug);

        if (teamAlreadyExists.isPresent()) {
            throw new TeamAlreadyExistsException();
        }

        Team team = this.createTeamFromRequest(requestTeam);
        Team savedTeam = this.teamRepository.save(team);
        return this.createResponseFromTeam(savedTeam);
    }

    public List<ResponseTeamByLeague> findTeamsByLeague(String leagueSlug) {

        Optional<League> leagueExists = this.leagueRepository.findBySlug(leagueSlug);

        if (leagueExists.isPresent()) {
            Optional<List<Team>> teams = this.teamRepository.findAllByLeagueId(leagueExists.get().getId());
            if (teams.isPresent()) {
                return this.createResponseTeamsByLeague(teams.get());
            }
            throw new TeamNotFoundException();
        }

        throw new LeagueNotFoundException();
    }

    @Transactional
    public List<ResponseTeamByLeague> createManyTeamsByLeague(RequestManyTeamsByLeague requestManyTeamsByLeague) {
        League league = this.leagueRepository.findBySlug(requestManyTeamsByLeague.getLeagueSlug())
                .orElseThrow(LeagueNotFoundException::new);

        List<Team> newTeams = new ArrayList<>();

        for (String s: requestManyTeamsByLeague.getTeamsNames()) {
            Optional<Team> teamAlreadyExists = this.teamRepository.findBySlug(s);
            if (teamAlreadyExists.isEmpty()) {
                Team team = new Team();
                team.setLeague(league);
                team.setName(s);
                team.setSlug(SlugGenerator.generateSlug(s));
                Team savedTeam = this.teamRepository.save(team);
                newTeams.add(savedTeam);
            }
        }

        return this.createResponseTeamsByLeague(newTeams);
    }

    public List<ResponseTeam> listTeams() {
        List<Team> teams = this.teamRepository.findAll();
        List<ResponseTeam> dtos = new ArrayList<>();

        for (Team t: teams) {
            ResponseTeam dto = new ResponseTeam();
            dto.setId(t.getId());
            dto.setName(t.getName());
            dto.setLeagueName(t.getLeague().getName());
        }
        return dtos;
    }

    public int countPlayerByTeam(String teamSlug) {
        if (this.teamRepository.findBySlug(teamSlug).isEmpty()) {
            throw new TeamNotFoundException();
        }

        return this.teamRepository.countPlayersByTeamSlug(teamSlug);
    }

    @Transactional
    public void addPlayersToTeam(String teamSlug, RequestAddPlayersToTeam requestAddPlayersToTeam) {
        Team team = this.teamRepository.findBySlug(teamSlug)
                .orElseThrow(TeamNotFoundException::new);

        for (String s: requestAddPlayersToTeam.getPlayersSlugs()) {
            Player player = this.playerService.createPlayerFromName(s);
            this.playerService.addTeamToPlayer(player.getSlug(), teamSlug);
        }
    }

    public void listAllPlayersByTeam(String teamSlug) {
        // to do
    }

    private Team createTeamFromRequest(RequestTeam requestTeam) {
       try {
           Team team = new Team();
           team.setName(requestTeam.getName());

           League league;
           if (requestTeam.getLeagueId() != null) {
               league = this.leagueRepository.findById(requestTeam.getLeagueId())
                       .orElseThrow(LeagueNotFoundException::new);
           } else {
               league = this.leagueRepository.findBySlug(requestTeam.getLeagueSlug())
                       .orElseThrow(LeagueNotFoundException::new);
           }
           team.setLeague(league);
           team.setSlug(SlugGenerator.generateSlug(team.getName()));

           return team;
       } catch (Exception e) {
           throw new InvalidTeamRequestException();
       }
    }

    private ResponseTeam createResponseFromTeam(Team team) {
        ResponseTeam dto = new ResponseTeam();
        dto.setId(team.getId());
        dto.setName(team.getName());
        dto.setLeagueName(team.getLeague().getName());
        dto.setSlug(team.getSlug());
        return dto;
    }

    private List<ResponseTeamByLeague> createResponseTeamsByLeague(List<Team> teams) {
        List<ResponseTeamByLeague> list = new ArrayList<>();
        for(Team t: teams) {
            ResponseTeamByLeague resp = new ResponseTeamByLeague();
            resp.setId(t.getId());
            resp.setName(t.getName());
            resp.setSlug(t.getSlug());
            list.add(resp);
        }
        return list;
    }
}
