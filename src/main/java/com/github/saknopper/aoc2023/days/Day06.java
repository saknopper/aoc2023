package com.github.saknopper.aoc2023.days;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Day06 extends Day {
    private static final URL INPUT_RESOURCE = Day03.class.getClassLoader().getResource("day06.txt");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("(\\d+)");

    @Override
    public String getAnswerPartOne() throws Exception {
        List<String> lines = List.of();
        try (var stream = Files.lines(Paths.get(INPUT_RESOURCE.toURI()))) {
            lines = stream.toList();
        }

        List<RaceStat> raceHistory = parseRaceHistory(lines);

        return String.valueOf(getPossibleWinsMultiplied(raceHistory));
    }

    @Override
    public String getAnswerPartTwo() throws Exception {
        List<String> lines = List.of();
        try (var stream = Files.lines(Paths.get(INPUT_RESOURCE.toURI()))) {
            lines = stream
                    .map(s -> s.replace("Time:", "").replace("Distance:", "").replaceAll("\\s+", ""))
                    .toList();
        }

        List<RaceStat> raceHistory = parseRaceHistory(lines);

        return String.valueOf(getPossibleWinsMultiplied(raceHistory));
    }

    private static long getPossibleWinsMultiplied(List<RaceStat> raceStats) {
        return raceStats.stream().mapToLong(rh -> {
            long possibleWins = 0L;
            for (int i = 0; i <= rh.duration; i++) {
                var remaining = rh.duration - i;
                if (remaining * i > rh.distance)
                    possibleWins++;
            }
            return possibleWins;
        }).reduce(1L, Math::multiplyExact);
    }

    private static List<RaceStat> parseRaceHistory(List<String> lines) {
        var durations = NUMBER_PATTERN.matcher(lines.get(0)).results().map(MatchResult::group).map(Long::valueOf).toList();
        var distances = NUMBER_PATTERN.matcher(lines.get(1)).results().map(MatchResult::group).map(Long::valueOf).toList();

        var history = new ArrayList<RaceStat>();
        for (int i = 0; i < durations.size(); i++)
            history.add(new RaceStat(durations.get(i), distances.get(i)));

        return history;
    }

    record RaceStat(long duration, long distance) { }
}