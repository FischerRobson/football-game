package com.football.Football.Game.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import com.football.Football.Game.models.Country;
import com.football.Football.Game.repositories.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class) // For fix CountryRepository NullPointer
public class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryService countryService;

    private List<Country> mockCountries;

    @BeforeEach
    public void setup() {
        Country country1 = new Country();
        country1.setId(UUID.randomUUID());
        country1.setName("USA");
        country1.setAbbreviation("US");

        Country country2 = new Country();
        country2.setId(UUID.randomUUID());
        country2.setName("Canada");
        country2.setAbbreviation("CA");

        mockCountries = Arrays.asList(country1, country2);
    }

    @Test
    public void testCreateCountries() throws Exception {
        when(countryRepository.saveAll(any(List.class))).thenReturn(mockCountries);

        List<Country> savedCountries = countryService.createCountries(mockCountries);

        verify(countryRepository).saveAll(mockCountries);

        assertThat(savedCountries).isNotNull();
        assertThat(savedCountries.size()).isEqualTo(2);
        assertThat(savedCountries.get(0).getName()).isEqualTo("USA");
    }

    @Test
    public void testCreateCountriesWithError() {
        when(countryRepository.saveAll(any(List.class))).thenThrow(new RuntimeException("Database error"));


        Exception exception = assertThrows(Exception.class, () -> {
            countryService.createCountries(mockCountries);
        });

        assertThat(exception.getMessage()).isEqualTo("Failed to create countries");
    }

    @Test
    public void testListCountries() {
        when(countryRepository.findAll()).thenReturn(mockCountries);

        List<Country> countries = countryService.listCountries();

        assertThat(countries).isNotNull();
        assertThat(countries.size()).isEqualTo(2);
        assertThat(countries).hasSize(2).extracting(Country::getName).contains("USA", "Canada");
    }

}
