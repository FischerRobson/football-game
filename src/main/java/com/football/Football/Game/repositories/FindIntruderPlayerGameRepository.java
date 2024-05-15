package com.football.Football.Game.repositories;

import com.football.Football.Game.models.FindIntrudePlayerGame;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface FindIntruderPlayerGameRepository extends JpaRepository<FindIntrudePlayerGame, UUID> {

    @Transactional
    void deleteByCreatedAtBefore(LocalDateTime dateTime);

}
