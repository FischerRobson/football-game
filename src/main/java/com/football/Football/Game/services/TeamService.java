package com.football.Football.Game.services;

import com.football.Football.Game.exceptions.LeagueNotFoundException;
import com.football.Football.Game.exceptions.TeamAlreadyExistsException;
import com.football.Football.Game.models.League;
import com.football.Football.Game.models.Team;
import com.football.Football.Game.models.dtos.request.RequestTeam;
import com.football.Football.Game.models.dtos.response.ResponseTeam;
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
        Team team = new Team();
        team.setName(requestTeam.getName());

        if (requestTeam.getLeagueSlug().isEmpty()) {
            League league = this.leagueRepository.findById(requestTeam.getLeagueId())
                    .orElseThrow(LeagueNotFoundException::new);
            team.setLeague(league);
        } else {
            League league = this.leagueRepository.findBySlug(requestTeam.getLeagueSlug())
                    .orElseThrow(LeagueNotFoundException::new);
            team.setLeague(league);
        }

        team.setSlug(SlugGenerator.generateSlug(team.getName()));
        return team;
    }

    private ResponseTeam createResponseFromTeam(Team team) {
        ResponseTeam dto = new ResponseTeam();
        dto.setId(team.getId());
        dto.setName(team.getName());
        dto.setLeagueName(team.getLeague().getName());
        dto.setSlug(team.getSlug());
        return dto;
    }
}
