package com.football.Football.Game.models;


import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity(name = "leagues")
public class League {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @JoinColumn(name = "countryId")
    @ManyToOne
    private Country country;
}


