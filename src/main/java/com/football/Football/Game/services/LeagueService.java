package com.football.Football.Game.services;

import com.football.Football.Game.exceptions.CountryNotFoundException;
import com.football.Football.Game.exceptions.InvalidLeagueRequestException;
import com.football.Football.Game.exceptions.LeagueAlreadyExistsException;
import com.football.Football.Game.models.Country;
import com.football.Football.Game.models.League;
import com.football.Football.Game.models.dtos.request.RequestLeague;
import com.football.Football.Game.repositories.CountryRepository;
import com.football.Football.Game.repositories.LeagueRepository;
import com.football.Football.Game.utils.SlugGenerator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LeagueService {

    @Autowired
    LeagueRepository leagueRepository;

    @Autowired
    CountryRepository countryRepository;

    @Transactional
    public List<League> createLeagues(List<RequestLeague> dtos) throws Exception {
        try {
            List<League> leagues = dtos.stream()
                    .map(this::createLeagueFromRequest)
                    .collect(Collectors.toList());
            return leagueRepository.saveAll(leagues);
        } catch (Exception e) {
            throw new Exception("Failed to create leagues");
        }
    }

    public League createLeague(RequestLeague dto) {
        Optional<League> leagueAlreadyExists =
                this.leagueRepository.findBySlug(SlugGenerator.generateSlug(dto.getName()));

        if (leagueAlreadyExists.isPresent()) {
            throw new LeagueAlreadyExistsException();
        }

        League league = this.createLeagueFromRequest(dto);

        return this.leagueRepository.save(league);
    }

    public List<League> listLeagues() {
        return this.leagueRepository.findAll();
    }

    private League createLeagueFromRequest(RequestLeague requestLeague) throws InvalidLeagueRequestException {
        try {
            League league = new League();
            league.setName(requestLeague.getName());
            league.setSlug(SlugGenerator.generateSlug(requestLeague.getName()));

            Country country;
            if (requestLeague.getCountryId() != null) {
                country = this.countryRepository.findById(requestLeague.getCountryId())
                        .orElseThrow(CountryNotFoundException::new);
            } else {
                country = this.countryRepository.findByAbbreviation(requestLeague.getCountryAbbreviation())
                        .orElseThrow(CountryNotFoundException::new);
            }
            league.setCountry(country);

            return league;
        } catch (Exception e) {
            throw new InvalidLeagueRequestException();
        }
    }
}
