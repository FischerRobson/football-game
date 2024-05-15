package com.football.Football.Game.controllers;


import com.football.Football.Game.enums.GameType;
import com.football.Football.Game.models.games.PlayFindIntrudePlayerGame;
import com.football.Football.Game.models.games.PlayGuessTeamGame;
import com.football.Football.Game.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    GameService gameService;

    @GetMapping("/new/guess-team")
    public ResponseEntity createGuessTeamGame() {
        try {
            Object game = this.gameService.createGame(GameType.GUESS_TEAM);
            return ResponseEntity.status(HttpStatus.OK).body(game);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/play/guess-team")
    public ResponseEntity playGuessTeamGame(@RequestBody PlayGuessTeamGame input) {
        try {
            Object output = this.gameService.playGame(GameType.GUESS_TEAM, input);
            return ResponseEntity.status(HttpStatus.OK).body(output);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }


    @GetMapping("/new/find-intruder")
    public ResponseEntity createFindIntruderPlayerGame() {
        try {
            Object game = this.gameService.createGame(GameType.FIND_INTRUDER);
            return ResponseEntity.status(HttpStatus.OK).body(game);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/play/find-intruder")
    public ResponseEntity playFindIntrudePlayerGame(@RequestBody PlayFindIntrudePlayerGame input) {
        try {
            Object output = this.gameService.playGame(GameType.FIND_INTRUDER, input);
            return ResponseEntity.status(HttpStatus.OK).body(output);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

}
