package com.github.saknopper.aoc2023.days;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Day05 extends Day {
    private static final URL INPUT_RESOURCE = Day03.class.getClassLoader().getResource("day05.txt");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("(\\d+)");

    @Override
    public String getAnswerPartOne() throws Exception {
        List<String> lines = List.of();
        try (var stream = Files.lines(Paths.get(INPUT_RESOURCE.toURI()))) {
            lines = stream.collect(Collectors.toCollection(ArrayList::new));
        }

        var seeds = NUMBER_PATTERN.matcher(lines.removeFirst()).results().map(MatchResult::group).map(Long::valueOf).toList();
        var mappings = getMappings(lines);

        var minLocation = seeds.stream().parallel().map(nr -> {
            long seedNr = nr;
            for (var m : mappings) {
                seedNr = m.transform(seedNr);
            }
            return seedNr;
        }).mapToLong(Long::valueOf).min().orElseThrow();

        return String.valueOf(minLocation);
    }

    @Override
    public String getAnswerPartTwo() throws Exception {
        List<String> lines = List.of();
        try (var stream = Files.lines(Paths.get(INPUT_RESOURCE.toURI()))) {
            lines = stream.collect(Collectors.toCollection(ArrayList::new));
        }

        var seedRanges = NUMBER_PATTERN.matcher(lines.removeFirst()).results().map(MatchResult::group).map(Long::valueOf).collect(Collectors.toCollection(ArrayList::new));
        var ranges = new ArrayList<Range>();
        while (!seedRanges.isEmpty()) {
            var start = seedRanges.removeFirst();
            var end = start + seedRanges.removeFirst();
            ranges.add(new Range(start, end));
        }
        var mappings = getMappings(lines);

        var minLocation = ranges.stream().flatMapToLong(r -> LongStream.range(r.start, r.end)).parallel().map(nr -> {
            //System.out.println("Handling: " + nr);
            long seedNr = nr;
            for (var m : mappings) {
                seedNr = m.transform(seedNr);
            }
            return seedNr;
        }).min().orElseThrow();

        return String.valueOf(minLocation);
    }

    private static List<Mapping> getMappings(List<String> lines) {
        var mappings = new ArrayList<Mapping>();

        List<Long> sources = List.of();
        List<Long> destinations = List.of();
        List<Long> ranges = List.of();

        lines.removeFirst();
        while (!lines.isEmpty()) {
            var line = lines.removeFirst();
            if (line.isBlank()) {
                mappings.add(new Mapping(sources, destinations, ranges));
            } else if (line.endsWith("map:")) {
                sources = new ArrayList<>();
                destinations = new ArrayList<>();
                ranges = new ArrayList<>();
            } else {
                var nrs = NUMBER_PATTERN.matcher(line).results().map(MatchResult::group).map(Long::valueOf).toList();
                destinations.add(nrs.get(0));
                sources.add(nrs.get(1));
                ranges.add(nrs.get(2));
            }
        }
        mappings.add(new Mapping(sources, destinations, ranges));

        return mappings;
    }

    public record Mapping(List<Long> sources, List<Long> destinations, List<Long> ranges) {
        public long transform(long input) {
            for (int i = 0; i < sources.size(); i++) {
                if (input >= sources.get(i) && input <= (sources.get(i) + ranges.get(i))) {
                    return Math.abs(destinations.get(i) - (sources.get(i)) + input);
                }
            }

            return input;
        }
    }

    public record Range(long start, long end) { }
}