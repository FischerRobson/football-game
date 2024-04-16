package com.football.Football.Game.controllers;

import com.football.Football.Game.models.Country;
import com.football.Football.Game.models.League;
import com.football.Football.Game.models.LeagueDTO;
import com.football.Football.Game.services.LeagueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leagues")
public class LeagueController {

    @Autowired
    LeagueService leagueService;

    @GetMapping
    public ResponseEntity list() {
        try {
            List<League> leagues = this.leagueService.listLeagues();
            return ResponseEntity.status(HttpStatus.OK).body(leagues);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/create-many")
    public ResponseEntity createMany(@RequestBody List<LeagueDTO> body) {
        try {
            List<League> leagues = this.leagueService.createLeagues(body);
            return ResponseEntity.status(HttpStatus.CREATED).body(leagues);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping
    public ResponseEntity create(@RequestBody LeagueDTO body) {
        System.out.println(body);
        try {
            League league = this.leagueService.createLeague(body);
            return ResponseEntity.status(HttpStatus.CREATED).body(league);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
