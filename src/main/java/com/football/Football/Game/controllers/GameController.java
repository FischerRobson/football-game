package com.football.Football.Game.controllers;


import com.football.Football.Game.models.GuessTeamGame;
import com.football.Football.Game.models.dtos.response.ResponseGuessTeamGame;
import com.football.Football.Game.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    GameService gameService;

    @GetMapping("/new")
    public ResponseEntity createGuessTeamGame() {
        try {
            ResponseGuessTeamGame game = this.gameService.createGuessTheTeamGame();
            return ResponseEntity.status(HttpStatus.OK).body(game);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

}
