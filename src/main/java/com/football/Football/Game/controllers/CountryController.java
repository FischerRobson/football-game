package com.football.Football.Game.controllers;

import com.football.Football.Game.models.Country;
import com.football.Football.Game.services.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/countries")
public class CountryController {

    @Autowired
    CountryService countryService;

    @GetMapping
    public ResponseEntity list() {
        try {
            List<Country> countries = this.countryService.listCountries();
            return ResponseEntity.status(HttpStatus.OK).body(countries);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/create-many")
    public ResponseEntity createMany(@RequestBody List<Country> body) {
        try {
            List<Country> countries = this.countryService.createCountries(body);
            return ResponseEntity.status(HttpStatus.CREATED).body(countries);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}

