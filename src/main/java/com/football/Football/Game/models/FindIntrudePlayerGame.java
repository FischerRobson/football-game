package com.football.Football.Game.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @CreationTimestamp
    private LocalDateTime createdAt;

    public FindIntrudePlayerGame() {
        this.playersNames = new ArrayList<>();
    }

    public void addPlayerName(String playerName) {
        this.playersNames.add(playerName);
    }

}
