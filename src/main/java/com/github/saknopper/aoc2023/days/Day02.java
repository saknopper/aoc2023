package com.github.saknopper.aoc2023.days;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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
        var cubeSets = Arrays.asList(splitted[1].split("; "));

        if (colorLimits.entrySet().stream()
                .anyMatch(limit -> maxCubesOfColor(cubeSets, limit.getKey()) > limit.getValue()))
            return 0;

        return Integer.valueOf(splitted[0].split(" ")[1]);
    }

    private static final Long findMaxPowerInGame(String line) {
        var splitted = line.split(": ");
        var cubeSets = Arrays.asList(splitted[1].split("; "));

        return List.of("red", "green", "blue").stream()
                .mapToLong(color -> maxCubesOfColor(cubeSets, color))
                .reduce(1, Math::multiplyExact);
    }

    private static final Long maxCubesOfColor(List<String> sets, String color) {
        return sets.stream()
                .flatMap(set -> Stream.of(set.split(", ")))
                .filter(c -> c.split(" ")[1].contentEquals(color))
                .map(c -> c.split(" ")[0])
                .mapToLong(Long::valueOf)
                .max().orElse(0L);
    }
}
