package com.football.Football.Game.utils;

import java.text.Normalizer;

public class SlugGenerator {

    public static String generateSlug(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        // Normalize the string: remove accents and special characters, replace spaces with hyphens
        String slug = Normalizer.normalize(input, Normalizer.Form.NFD);
        slug = slug.replaceAll("[\\p{InCombiningDiacriticalMarks}]", ""); // Remove diacritics (accents)
        slug = slug.replaceAll("[^\\p{Alnum} ]", ""); // Remove non-alphanumeric characters except spaces
        slug = slug.trim().replaceAll("\\s+", "-").toLowerCase(); // Replace spaces with hyphens and convert to lower case

        return slug;
    }

}
