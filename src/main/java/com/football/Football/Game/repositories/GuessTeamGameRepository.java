package com.football.Football.Game.repositories;

import com.football.Football.Game.models.GuessTeamGame;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface GuessTeamGameRepository extends JpaRepository<GuessTeamGame, UUID> {

    @Transactional
    void deleteByCreatedAtBefore(LocalDateTime dateTime);
}
