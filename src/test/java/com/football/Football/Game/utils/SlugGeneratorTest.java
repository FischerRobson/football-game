package com.football.Football.Game.utils;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
public class SlugGeneratorTest {

    @Test
    public void testEmptySlug() {
        String response = SlugGenerator.generateSlug("");
        assertThat(response).isEqualTo("");
    }

    @Test
    public void testNullSlug() {
        String response = SlugGenerator.generateSlug(null);
        assertThat(response).isEqualTo("");
    }

    @Test
    public void testNameSlug() {
        String response = SlugGenerator.generateSlug("John Doe");
        assertThat(response).isEqualTo("john-doe");
    }

    @Test
    public void testSpecialCharacterSlug() {
        String response = SlugGenerator.generateSlug("john @doe");
        assertThat(response).isEqualTo("john-doe");
    }

    @Test
    public void testAccentSlug() {
        String response = SlugGenerator.generateSlug("Café Münchên");
        assertThat(response).isEqualTo("cafe-munchen");
    }


}
