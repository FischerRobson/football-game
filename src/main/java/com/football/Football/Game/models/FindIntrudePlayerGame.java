package com.football.Football.Game.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity(name = "find_intrude_player_game")
public class FindIntrudePlayerGame {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID gameId;

    private List<String> playersNames;

    private String answer;

}
