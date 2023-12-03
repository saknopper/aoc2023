package com.github.saknopper.aoc2023.days;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03 extends Day {
    private static final URL INPUT_RESOURCE = Day03.class.getClassLoader().getResource("day03.txt");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("(\\d+)");

    @Override
    public String getAnswerPartOne() throws Exception {
        List<String> input = Files.lines(Paths.get(INPUT_RESOURCE.toURI())).toList();

        Map<Point, List<Long>> symbols = extractSpecialSymbols(input, c -> !Character.isDigit(c) && c != '.');
        findPartsAdjacentToSymbols(input, symbols);

        return String.valueOf(symbols.values().stream().filter(partNrs -> !partNrs.isEmpty())
                .mapToLong(partNrs -> partNrs.stream().mapToLong(Long::valueOf).sum()).sum());
    }

    @Override
    public String getAnswerPartTwo() throws Exception {
        List<String> input = Files.lines(Paths.get(INPUT_RESOURCE.toURI())).toList();

        Map<Point, List<Long>> gears = extractSpecialSymbols(input, c -> c == '*');
        findPartsAdjacentToSymbols(input, gears);

        return String.valueOf(gears.values().stream().filter(partNrs -> partNrs.size() == 2)
                .mapToLong(partNrs -> partNrs.stream().reduce(1L, Math::multiplyExact)).sum());
    }

    private static Map<Point, List<Long>> extractSpecialSymbols(List<String> input, Predicate<Character> predicate) {
        Map<Point, List<Long>> symbols = new HashMap<>();

        var lineLength = input.get(0).length();
        for (int i = 0; i < input.size(); i++) {
            for (int j = 0; j < lineLength; j++) {
                var c = input.get(i).charAt(j);
                if (predicate.test(c)) {
                    symbols.put(new Point(i, j), new ArrayList<>());
                }
            }
        }

        return symbols;
    }

    private static void findPartsAdjacentToSymbols(List<String> input, Map<Point, List<Long>> symbols) {
        for (int line = 0; line < input.size(); line++) {
            final int _line = line;
            Matcher matcher = NUMBER_PATTERN.matcher(input.get(line));
            matcher.results().forEach(r -> {
                for (int i = _line - 1; i <= _line + 1; i++) {
                    for (int j = r.start() - 1; j <= r.end(); j++) {
                        var p = new Point(i, j);
                        if (symbols.containsKey(p)) {
                            symbols.get(p).add(Long.valueOf(r.group()));
                        }
                    }
                }
            });
        }
    }

    public record Point(int i, int j) { }
}