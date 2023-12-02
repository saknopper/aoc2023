package com.github.saknopper.aoc2023.days;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Day02 extends Day {
    @Override
    public String getAnswerPartOne() throws Exception {
        final Map<String, Long> colorLimits = Map.of("red", 12L, "green", 13L, "blue", 14L);

        Path path = Paths.get(getClass().getClassLoader().getResource("day02.txt").toURI());
        var sum = Files.lines(path).mapToInt(line -> isGamePossible(line, colorLimits)).sum();

        return String.valueOf(sum);
    }

    @Override
    public String getAnswerPartTwo() throws Exception {
        Path path = Paths.get(getClass().getClassLoader().getResource("day02.txt").toURI());
        var sum = Files.lines(path).mapToLong(Day02::findMaxPowerInGame).sum();

        return String.valueOf(sum);
    }

    private static final Integer isGamePossible(String line, Map<String, Long> colorLimits) {
        var splitted = line.split(": ");
        var gameInfo = splitted[0];
        var cubeInfo = splitted[1];
        var cubeSets = Arrays.asList(cubeInfo.split("; "));

        if (colorLimits.entrySet().stream()
                .anyMatch(limit -> cubeSets.stream().map(set -> maxCubesOfColor(set, limit.getKey()))
                .max(Long::compareTo).orElseThrow() > limit.getValue()))
            return 0;

        return Integer.valueOf(gameInfo.split(" ")[1]);
    }

    private static final Long findMaxPowerInGame(String line) {
        var splitted = line.split(": ");
        var cubeInfo = splitted[1];
        var cubeSets = Arrays.asList(cubeInfo.split("; "));

        return List.of("red", "green", "blue").stream()
                .mapToLong(color -> cubeSets.stream().map(set -> maxCubesOfColor(set, color)).max(Long::compareTo).orElseThrow())
                .reduce(1, Math::multiplyExact);
    }

    private static final Long maxCubesOfColor(String set, String color) {
        return Arrays.asList(set.split(", ")).stream()
                .filter(c -> c.split(" ")[1].contentEquals(color))
                .map(c -> c.split(" ")[0])
                .mapToLong(Long::valueOf)
                .reduce(1, Math::multiplyExact);
    }
}
