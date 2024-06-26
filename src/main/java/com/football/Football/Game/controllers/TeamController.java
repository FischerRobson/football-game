package com.football.Football.Game.controllers;

import com.football.Football.Game.models.dtos.request.RequestAddPlayersToTeam;
import com.football.Football.Game.models.dtos.request.RequestManyTeamsByLeague;
import com.football.Football.Game.models.dtos.request.RequestTeam;
import com.football.Football.Game.models.dtos.response.ResponseTeam;
import com.football.Football.Game.models.dtos.response.ResponseTeamByLeague;
import com.football.Football.Game.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    TeamService teamService;

    @PostMapping
    public ResponseEntity create(@RequestBody RequestTeam body) {

        try {
            ResponseTeam team = this.teamService.createTeam(body);
            return ResponseEntity.status(HttpStatus.CREATED).body(team);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/create-many")
    public ResponseEntity createManyTeamsByLeague(@RequestBody RequestManyTeamsByLeague body) {
        try {
            List<ResponseTeamByLeague> teams = this.teamService.createManyTeamsByLeague(body);
            return ResponseEntity.status(HttpStatus.CREATED).body(teams);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/{teamSlug}/add-players")
    public ResponseEntity addPlayersToTeam(@PathVariable String teamSlug, @RequestBody RequestAddPlayersToTeam body) {
        try {
            this.teamService.addPlayersToTeam(teamSlug, body);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{leagueSlug}")
    public ResponseEntity findAllByLeague(@PathVariable String leagueSlug) {
        try {
            List<ResponseTeamByLeague> teams = this.teamService.findTeamsByLeague(leagueSlug);
            return ResponseEntity.status(HttpStatus.OK).body(teams);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
