package com.github.saknopper.aoc2023.days;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Day01 extends Day {
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
        return "TODO";
    }
}
