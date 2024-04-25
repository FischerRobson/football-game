package com.football.Football.Game.services;

import com.football.Football.Game.exceptions.PlayerNotFoundException;
import com.football.Football.Game.exceptions.TeamNotFoundException;
import com.football.Football.Game.models.Player;
import com.football.Football.Game.models.Team;
import com.football.Football.Game.models.dtos.request.RequestPlayer;
import com.football.Football.Game.models.dtos.response.ResponsePlayer;
import com.football.Football.Game.models.dtos.response.ResponseTeam;
import com.football.Football.Game.repositories.PlayerRepository;
import com.football.Football.Game.repositories.TeamRepository;
import com.football.Football.Game.utils.SlugGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    public Player createPlayerFromName(String name) {
        Optional<Player> player = this.playerRepository.findBySlug(SlugGenerator.generateSlug(name));
        if (player.isEmpty()) {
            System.out.println("not found in db");
            Player newPlayer = new Player();
            newPlayer.setName(name);
            newPlayer.setSlug(SlugGenerator.generateSlug(name));
            return this.playerRepository.save(newPlayer);
        }
        return player.get();
    }

    public void addTeamToPlayer(String playerSlug, String teamSlug) {
        Player player = this.playerRepository.findBySlug(playerSlug)
                .orElseThrow(PlayerNotFoundException::new);

        Team team = this.teamRepository.findBySlug(teamSlug).orElseThrow(TeamNotFoundException::new);

        player.addTeam(team);

        this.playerRepository.save(player);
    }

    public boolean checkIfPlayerHasTeams(String playerSlug, Set<String> teamsSlugs) {
        Player player = this.playerRepository.findBySlug(playerSlug).orElseThrow(PlayerNotFoundException::new);
        Set<Team> teams = player.getTeams();

        Set<String> playerTeamSlugs = player.getTeams().stream()
                .map(Team::getSlug)
                .collect(Collectors.toSet());

        return playerTeamSlugs.containsAll(teamsSlugs);
    }

    public Player getRandomPlayer() {
        Optional<Player> player = this.playerRepository.findRandomPlayer();
        return player.get();
    }

    public ResponsePlayer addTeamsToPlayer(Set<String> teamsSlugs, String playerSlug) {
        Player player = this.playerRepository.findBySlug(playerSlug).orElseThrow(PlayerNotFoundException::new);

        Set<Team> teams = player.getTeams();

        Set<String> playerTeamSlugs = player.getTeams().stream()
                .map(Team::getSlug)
                .collect(Collectors.toSet());

        for (String slug: teamsSlugs) {
            if (!playerTeamSlugs.contains(slug)) {
                Team team = this.teamRepository.findBySlug(slug).orElseThrow(TeamNotFoundException::new);
                teams.add(team);
            }
        }
        player.setTeams(teams);

        Player updatedPlayer = this.playerRepository.save(player);
        return this.createResponseFromPlayer(updatedPlayer);
    }

    private Player createPlayerFromRequest(RequestPlayer requestPlayer) {
        Player player = new Player();
        player.setName(requestPlayer.getName());
        player.setSlug(SlugGenerator.generateSlug(requestPlayer.getName()));

        Set<Team> teams = new HashSet<>();
        if (requestPlayer.getTeamsSlugs().isEmpty()) {
            for (UUID id: requestPlayer.getTeamsIds()) {
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
        responsePlayer.setSlug(player.getSlug());

        List<ResponseTeam> responseTeams = new ArrayList<>();
        for (Team team: player.getTeams()) {
            ResponseTeam responseTeam = new ResponseTeam();
            responseTeam.setId(team.getId());
            responseTeam.setName(team.getName());
            responseTeam.setLeagueName(team.getLeague().getName());
            responseTeam.setSlug(team.getSlug());
            responseTeams.add(responseTeam);
        }
        responsePlayer.setTeams(responseTeams);
        return responsePlayer;
    }
}
