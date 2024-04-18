package com.football.Football.Game.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.football.Football.Game.exceptions.InvalidLeagueRequestException;
import com.football.Football.Game.exceptions.LeagueAlreadyExistsException;
import com.football.Football.Game.models.Country;
import com.football.Football.Game.models.League;
import com.football.Football.Game.models.dtos.request.RequestLeague;
import com.football.Football.Game.repositories.CountryRepository;
import com.football.Football.Game.repositories.LeagueRepository;
import com.football.Football.Game.utils.SlugGenerator;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class LeagueServiceTest {

    @Mock
    private LeagueRepository leagueRepository;

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private LeagueService leagueService;

    private RequestLeague requestLeagueById;

    private RequestLeague requestLeagueByAbbreviation;

    private League league;

    private League leagueByAbbreviation;

    private Country country;

    @BeforeEach
    void setUp() {
        country = new Country();
        country.setId(UUID.randomUUID());
        country.setName("England");
        country.setAbbreviation("ENG");

        requestLeagueById = new RequestLeague();
        requestLeagueById.setName("Premier League");
        requestLeagueById.setCountryId(country.getId());

        requestLeagueByAbbreviation = new RequestLeague();
        requestLeagueByAbbreviation.setName("Championship");
        requestLeagueByAbbreviation.setCountryAbbreviation("ENG");

        league = new League();
        league.setName(requestLeagueById.getName());
        league.setSlug(SlugGenerator.generateSlug(requestLeagueById.getName()));
        league.setCountry(country);

        leagueByAbbreviation = new League();
        leagueByAbbreviation.setName(requestLeagueByAbbreviation.getName());
        leagueByAbbreviation.setSlug(SlugGenerator.generateSlug(requestLeagueByAbbreviation.getName()));
        leagueByAbbreviation.setCountry(country);

    }

    @Test
    void testCreateLeagueWithNonExistentSlug() {
        when(leagueRepository.findBySlug(anyString())).thenReturn(Optional.empty());
        when(leagueRepository.save(any(League.class))).thenReturn(league);
        when(countryRepository.findById(requestLeagueById.getCountryId()))
                .thenReturn(Optional.of(country));

        League savedLeague = leagueService.createLeague(requestLeagueById);

        assertNotNull(savedLeague);
        assertEquals("premier-league", savedLeague.getSlug());
        verify(leagueRepository).save(any(League.class)); // Ensure save is called
    }

    @Test
    void testCreateLeagueWithExistingSlug() {
        when(leagueRepository.findBySlug("premier-league")).thenReturn(Optional.of(league));

        assertThrows(LeagueAlreadyExistsException.class, () -> leagueService.createLeague(requestLeagueById));
    }

    @Test
    void testCreateLeagueFromAbbreviation() {
        when(leagueRepository.findBySlug("championship")).thenReturn(Optional.empty());
        when(leagueRepository.save(any(League.class))).thenReturn(leagueByAbbreviation);
        when(countryRepository.findByAbbreviation("ENG"))
                .thenReturn(Optional.of(country));

        League savedLeague = leagueService.createLeague(requestLeagueByAbbreviation);

        assertNotNull(savedLeague);
        assertEquals("championship", savedLeague.getSlug());
        verify(countryRepository).findByAbbreviation("ENG");
    }

    @Test
    void testInvalidLeagueRequestException() {
        when(countryRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(InvalidLeagueRequestException.class, () -> leagueService.createLeague(requestLeagueById));
    }

    @Test
    void testCreateLeagues() throws Exception {
        when(countryRepository.findById(requestLeagueById.getCountryId()))
                .thenReturn(Optional.of(country));
        when(countryRepository.findByAbbreviation("ENG"))
                .thenReturn(Optional.of(country));

        List<RequestLeague> leagueDtos = Arrays.asList(requestLeagueById, requestLeagueByAbbreviation);
        List<League> leagues = Arrays.asList(league, new League());

        when(leagueRepository.saveAll(anyList())).thenReturn(leagues);

        List<League> createdLeagues = leagueService.createLeagues(leagueDtos);

        assertNotNull(createdLeagues);
        assertEquals(2, createdLeagues.size());
    }

    @Test
    void testListLeagues() {
        when(leagueRepository.findAll()).thenReturn(Arrays.asList(league));

        List<League> leagues = leagueService.listLeagues();

        assertNotNull(leagues);
        assertFalse(leagues.isEmpty());
        assertEquals("Premier League", leagues.get(0).getName());
    }
}
