package com.football.Football.Game.services;

import com.football.Football.Game.models.Country;
import com.football.Football.Game.models.League;
import com.football.Football.Game.models.LeagueDTO;
import com.football.Football.Game.repositories.CountryRepository;
import com.football.Football.Game.repositories.LeagueRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeagueService {

    @Autowired
    LeagueRepository leagueRepository;

    @Autowired
    CountryRepository countryRepository;

    @Transactional
    public List<League> createLeagues(List<LeagueDTO> dtos) throws Exception {
        try {
            List<League> leagues = dtos.stream()
                    .map(this::createLeagueFromDTO)
                    .collect(Collectors.toList());
            return leagueRepository.saveAll(leagues);
        } catch (Exception e) {
            throw new Exception("Failed to create leagues");
        }
    }

    public League createLeague(LeagueDTO dto) {
        League league = this.createLeagueFromDTO(dto);
        return this.leagueRepository.save(league);
    }

    public List<League> listLeagues() {
        return this.leagueRepository.findAll();
    }

    private League createLeagueFromDTO(LeagueDTO dto) {
        League league = new League();
        league.setName(dto.getName());

        Country country = this.countryRepository.findById(dto.getCountryId())
                .orElseThrow(() -> new RuntimeException("Country not found"));
        league.setCountry(country);
        return league;
    }
}
