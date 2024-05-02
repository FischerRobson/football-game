package com.football.Football.Game.repositories;

import com.football.Football.Game.models.GuessTeamGame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GuessTeamGameRepository extends JpaRepository<GuessTeamGame, UUID> {
}
