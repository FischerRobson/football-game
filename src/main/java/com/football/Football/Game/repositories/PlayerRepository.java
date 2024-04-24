package com.football.Football.Game.repositories;

import com.football.Football.Game.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PlayerRepository extends JpaRepository<Player, UUID> {

    @Query(value = "SELECT p.* FROM players p " +
            "JOIN player_team pt ON p.id = pt.player_id " +
            "JOIN teams t ON pt.team_id = t.id " +
            "WHERE t.slug IN (:slugs) " +
            "GROUP BY p.id " +
            "HAVING COUNT(DISTINCT t.id) = 2", nativeQuery = true)
    List<Player> findPlayersByTeamSlugs(@Param("slugs") List<String> slugs);

}
