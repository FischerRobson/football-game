package com.football.Football.Game.repositories;

import com.football.Football.Game.models.Player;
import com.football.Football.Game.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface PlayerRepository extends JpaRepository<Player, UUID> {

    @Query(value = "SELECT p.* FROM players p " +
            "JOIN player_team pt ON p.id = pt.player_id " +
            "JOIN teams t ON pt.team_id = t.id " +
            "WHERE t.slug IN (:slugs) " +
            "GROUP BY p.id " +
            "HAVING COUNT(DISTINCT t.id) = :size", nativeQuery = true)
    List<Player> findPlayersByTeamSlugs(@Param("slugs") Set<String> slugs, int size);

    @Query(value = "SELECT * FROM players ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<Player> findRandomPlayer();

    Optional<Player> findBySlug(String slug);

}
