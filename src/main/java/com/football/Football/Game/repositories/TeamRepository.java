package com.football.Football.Game.repositories;

import com.football.Football.Game.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeamRepository extends JpaRepository<Team, UUID> {

    Optional<Team> findBySlug(String slug);

    Optional<List<Team>> findAllByLeagueId(UUID leagueId);

}
