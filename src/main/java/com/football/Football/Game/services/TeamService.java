package com.football.Football.Game.services;

import com.football.Football.Game.exceptions.InvalidTeamRequestException;
import com.football.Football.Game.exceptions.LeagueNotFoundException;
import com.football.Football.Game.exceptions.TeamAlreadyExistsException;
import com.football.Football.Game.exceptions.TeamNotFoundException;
import com.football.Football.Game.models.League;
import com.football.Football.Game.models.Team;
import com.football.Football.Game.models.dtos.request.RequestTeam;
import com.football.Football.Game.models.dtos.response.ResponseTeam;
import com.football.Football.Game.models.dtos.response.ResponseTeamByLeague;
import com.football.Football.Game.repositories.LeagueRepository;
import com.football.Football.Game.repositories.TeamRepository;
import com.football.Football.Game.utils.SlugGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    LeagueRepository leagueRepository;

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
        System.out.println(leagueExists.get());

        if (leagueExists.isPresent()) {
            Optional<List<Team>> teams = this.teamRepository.findAllByLeagueId(leagueExists.get().getId());
            System.out.println(teams.get());
            if (teams.isPresent()) {
                return this.createResponseTeamsByLeague(teams.get());
            }
            throw new TeamNotFoundException();
        }

        throw new LeagueNotFoundException();
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
