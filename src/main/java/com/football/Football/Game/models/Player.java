package com.football.Football.Game.models;

import jakarta.persistence.*;
import lombok.Data;


import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "player_team",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    Set<Team> teams;

    private String slug;

    public void addTeam(Team team) {
        if (this.teams == null || this.teams.isEmpty()) {
            this.teams = new HashSet<>();
        }
        this.teams.add(team);
    }
}
