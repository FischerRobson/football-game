package com.football.Football.Game.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Randomizer {

    public static List<Integer> generateRandomIndexes(int size) {
        Random random = new Random();
        int firstIndex = random.nextInt(size);
        int secondIndex;
        do {
            secondIndex = random.nextInt(size);
        } while (secondIndex == firstIndex);
        return Arrays.asList(firstIndex, secondIndex);
    }
}
