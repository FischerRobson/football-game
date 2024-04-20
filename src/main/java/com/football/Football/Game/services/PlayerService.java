package com.football.Football.Game.services;

import com.football.Football.Game.exceptions.TeamNotFoundException;
import com.football.Football.Game.models.Player;
import com.football.Football.Game.models.Team;
import com.football.Football.Game.models.dtos.request.RequestPlayer;
import com.football.Football.Game.models.dtos.response.ResponsePlayer;
import com.football.Football.Game.models.dtos.response.ResponseTeam;
import com.football.Football.Game.repositories.PlayerRepository;
import com.football.Football.Game.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    TeamRepository teamRepository;

    public ResponsePlayer createPlayer(RequestPlayer requestPlayer) {
        Player player = this.createPlayerFromRequest(requestPlayer);
        Player savedPlayer = this.playerRepository.save(player);
        return this.createResponseFromPlayer(savedPlayer);
    }

    private Player createPlayerFromRequest(RequestPlayer requestPlayer) {
        Player player = new Player();
        player.setName(requestPlayer.getName());

        Set<Team> teams = new HashSet<>();
        if (requestPlayer.getTeamsSlugs().isEmpty()) {
            for (UUID id: requestPlayer.getTeamsId()) {
                Team team = teamRepository.findById(id).orElseThrow(TeamNotFoundException::new);
                teams.add(team);
            }
        } else {
            for (String slug: requestPlayer.getTeamsSlugs()) {
                Team team = teamRepository.findBySlug(slug).orElseThrow(TeamNotFoundException::new);
                teams.add(team);
            }
        }
        player.setTeams(teams);

        return player;
    }

    private ResponsePlayer createResponseFromPlayer(Player player) {
        ResponsePlayer responsePlayer = new ResponsePlayer();
        responsePlayer.setId(player.getId());
        responsePlayer.setName(player.getName());

        List<ResponseTeam> responseTeams = new ArrayList<>();
        for (Team team: player.getTeams()) {
            ResponseTeam responseTeam = new ResponseTeam();
            responseTeam.setId(team.getId());
            responseTeam.setName(team.getName());
            responseTeam.setLeagueName(team.getLeague().getName());
            responseTeams.add(responseTeam);
        }
        responsePlayer.setTeams(responseTeams);
        return responsePlayer;
    }
}
