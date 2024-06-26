package com.football.Football.Game.repositories;

import com.football.Football.Game.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeamRepository extends JpaRepository<Team, UUID> {

    @Query(value = "SELECT COUNT (*) \n" +
            "FROM teams t\n" +
            "JOIN player_team pt ON t.id = pt.team_id\n" +
            "WHERE t.slug = :teamSlug", nativeQuery = true)
    Integer countPlayersByTeamSlug(String teamSlug);

    Optional<Team> findBySlug(String slug);

    Optional<List<Team>> findAllByLeagueId(UUID leagueId);

    @Query(value = "SELECT * FROM teams ORDER BY RANDOM() * EXTRACT(EPOCH FROM NOW()) LIMIT 1", nativeQuery = true)
    Team findRandomTeam();

}
