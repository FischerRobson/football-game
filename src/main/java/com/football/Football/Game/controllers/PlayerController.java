package com.football.Football.Game.controllers;

import com.football.Football.Game.models.Player;
import com.football.Football.Game.models.dtos.request.RequestPlayer;
import com.football.Football.Game.models.dtos.response.ResponsePlayer;
import com.football.Football.Game.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/players")
public class PlayerController {

    @Autowired
    PlayerService playerService;

    @PostMapping
    public ResponseEntity create(@RequestBody RequestPlayer body) {

        try {
            ResponsePlayer responsePlayer = this.playerService.createPlayer(body);
            return ResponseEntity.status(HttpStatus.CREATED).body(responsePlayer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
