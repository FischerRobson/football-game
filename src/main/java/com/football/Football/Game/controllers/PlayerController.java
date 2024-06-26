package com.football.Football.Game.controllers;


import com.football.Football.Game.models.dtos.request.RequestPlayer;
import com.football.Football.Game.models.dtos.request.RequestTeamsSlugs;
import com.football.Football.Game.models.dtos.response.ResponsePlayer;
import com.football.Football.Game.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayerController {

    @Autowired
    PlayerService playerService;

    @GetMapping("/team/{teamSlug}")
    public ResponseEntity findByTeam(@PathVariable String teamSlug) {
        try {
            List<String> names = this.playerService.findPlayersNameByTeamSlug(teamSlug);
            return ResponseEntity.status(HttpStatus.CREATED).body(names);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity create(@RequestBody RequestPlayer body) {
        try {
            ResponsePlayer responsePlayer = this.playerService.createPlayer(body);
            return ResponseEntity.status(HttpStatus.CREATED).body(responsePlayer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{playerSlug}")
    public ResponseEntity checkIfPlayerHasTeams(@PathVariable String playerSlug, @RequestParam List<String> teams) {
        try {
            Boolean matches = this.playerService.checkIfPlayerHasTeams(playerSlug, teams);
            return ResponseEntity.status(HttpStatus.OK).body(matches);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/{playerSlug}/add-teams")
    public ResponseEntity addTeamsToPlayer(@PathVariable String playerSlug, @RequestBody RequestTeamsSlugs body) {
        try {
            ResponsePlayer player = this.playerService.addTeamsToPlayer(body.getTeamsSlugs(), playerSlug);
            return ResponseEntity.status(HttpStatus.OK).body(player);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
