package com.github.saknopper.aoc2023.days;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Day01 extends Day {
    private static final Map<String, String> SPELLED_OUT_MAP =
        Map.of(
            "one", "1",
            "two", "2",
            "three", "3",
            "four", "4",
            "five", "5",
            "six", "6",
            "seven", "7",
            "eight", "8",
            "nine", "9"
        );

    @Override
    public String getAnswerPartOne() throws Exception {
        Path path = Paths.get(getClass().getClassLoader().getResource("day01.txt").toURI());
        var sum = Files.lines(path)
                .map(line -> line.chars().mapToObj(c -> (char) c).filter(Character::isDigit).toList())
                .map(digitList -> digitList.getFirst().toString() + digitList.getLast().toString())
                .mapToInt(Integer::valueOf)
                .sum();

        return String.valueOf(sum);
    }

    @Override
    public String getAnswerPartTwo() throws Exception {
        Path path = Paths.get(getClass().getClassLoader().getResource("day01.txt").toURI());
        var sum = Files.lines(path)
                .map(Day01::replaceSpelledOutNumbers)
                .map(line -> line.chars().mapToObj(c -> (char) c).filter(Character::isDigit).toList())
                .map(digitList -> digitList.getFirst().toString() + digitList.getLast().toString())
                .mapToLong(Long::valueOf)
                .sum();

        return String.valueOf(sum);
    }

    private static String replaceSpelledOutNumbers(String input) {
        var result = new StringBuilder(input);

        for (int i = 0; i < result.length(); i++) {
            for (var entry : SPELLED_OUT_MAP.entrySet()) {
                if (result.substring(i).startsWith(entry.getKey())) {
                    result.setCharAt(i, entry.getValue().charAt(0));
                }
            }
        }

        return result.toString();
    }
}
