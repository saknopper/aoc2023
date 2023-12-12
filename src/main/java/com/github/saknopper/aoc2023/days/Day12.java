package com.github.saknopper.aoc2023.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import one.util.streamex.IntStreamEx;

public class Day12 extends Day {
    private static final URL INPUT_RESOURCE = Day12.class.getClassLoader().getResource("day12.txt");

    private final List<SpringRecord> records;

    private final Map<SpringRecord, Long> cache = new ConcurrentHashMap<>(4096);

    public Day12() throws IOException, URISyntaxException {
        try (var stream = Files.lines(Paths.get(INPUT_RESOURCE.toURI()))) {
            records = stream.map(l -> {
                var splitted = l.split(" ");
                return new SpringRecord(splitted[0], Stream.of(splitted[1].split(",")).map(Integer::valueOf).toList());
            }).toList();
        }
    }

    @Override
    public String getAnswerPartOne() throws Exception {
        return String.valueOf(records.stream().mapToLong(this::countPermutations).sum());
    }

    @Override
    public String getAnswerPartTwo() throws Exception {
        return String.valueOf(records.stream().map(Day12::unfold).parallel().mapToLong(this::countPermutations).sum());
    }

    private static SpringRecord unfold(SpringRecord springRecord) {
        return new SpringRecord(IntStreamEx.range(5).mapToObj(i -> springRecord.conditions).joining("?"),
                IntStreamEx.range(5).flatMapToObj(i -> springRecord.damagedAmounts.stream()).toList());
    }

    private long countPermutations(SpringRecord springRecord) {
        if (cache.containsKey(springRecord))
            return cache.get(springRecord);

        String conditions = springRecord.conditions;
        List<Integer> damagedAmounts = springRecord.damagedAmounts;

        if (conditions.isBlank())
            return damagedAmounts.isEmpty() ? 1L : 0L;

        long permutations = 0;
        char firstChar = conditions.charAt(0);
        switch (firstChar) {
            case '.' -> permutations = countPermutations(new SpringRecord(conditions.substring(1), damagedAmounts));
            case '?' -> permutations = countPermutations(new SpringRecord("." + conditions.substring(1), damagedAmounts))
                        + countPermutations(new SpringRecord("#" + conditions.substring(1), damagedAmounts));
            case '#' -> {
                if (!damagedAmounts.isEmpty()) {
                    int damagedAmount = damagedAmounts.getFirst();
                    if (damagedAmount <= conditions.length()
                            && conditions.chars().limit(damagedAmount).allMatch(c -> c == '#' || c == '?')) {
                        var remainingDamagedAmounts = damagedAmounts.subList(1, damagedAmounts.size());
                        if (damagedAmount == conditions.length())
                            permutations = remainingDamagedAmounts.isEmpty() ? 1 : 0;
                        else if (conditions.charAt(damagedAmount) == '.')
                            permutations = countPermutations(new SpringRecord(conditions.substring(damagedAmount + 1), remainingDamagedAmounts));
                        else if (conditions.charAt(damagedAmount) == '?')
                            permutations = countPermutations(new SpringRecord("." + conditions.substring(damagedAmount + 1), remainingDamagedAmounts));
                    }
                }
            }
            default -> throw new RuntimeException("Unhandled first character");
        }

        cache.put(springRecord, permutations);

        return permutations;
    }

    record SpringRecord(String conditions, List<Integer> damagedAmounts) { }
}