package com.football.Football.Game.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @JoinColumn(name = "league_id")
    @ManyToOne
    private League league;

    private String slug;
}
