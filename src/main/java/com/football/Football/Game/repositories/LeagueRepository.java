package com.football.Football.Game.repositories;

import com.football.Football.Game.models.League;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LeagueRepository extends JpaRepository<League, UUID> {
}
