package com.football.Football.Game.controllers;

import com.football.Football.Game.models.dtos.request.RequestTeam;
import com.football.Football.Game.models.dtos.response.ResponseTeam;
import com.football.Football.Game.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
