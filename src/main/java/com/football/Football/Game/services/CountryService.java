package com.football.Football.Game.services;

import com.football.Football.Game.models.Country;
import com.football.Football.Game.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {

    @Autowired
    CountryRepository countryRepository;

    public List<Country> createCountries(List<Country> countries) throws Exception {
        try {
            return this.countryRepository.saveAll(countries);
        } catch (Exception e) {
            throw new Exception("Failed to create countries");
        }
    }

    public List<Country> listCountries() {
        return this.countryRepository.findAll();
    }
}
