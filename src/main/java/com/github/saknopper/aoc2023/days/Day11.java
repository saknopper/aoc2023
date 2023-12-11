package com.github.saknopper.aoc2023.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

import one.util.streamex.StreamEx;

public class Day11 extends Day {
    private static final URL INPUT_RESOURCE = Day11.class.getClassLoader().getResource("day11.txt");

    private final List<Position> originalGalaxies = new ArrayList<>();

    private final List<Long> emptyRowNrs;
    private final List<Long> emptyColNrs;

    public Day11() throws IOException, URISyntaxException {
        var lines = Files.readAllLines(Paths.get(INPUT_RESOURCE.toURI()));
        for (int i = 0; i < lines.size(); i++)
            for (int j = 0; j < lines.get(0).length(); j++)
                if (lines.get(i).charAt(j) == '#')
                    originalGalaxies.add(new Position(i, j));

        long iMin = originalGalaxies.stream().mapToLong(Position::i).min().orElseThrow();
        long iMax = originalGalaxies.stream().mapToLong(Position::i).max().orElseThrow();
        long jMin = originalGalaxies.stream().mapToLong(Position::j).min().orElseThrow();
        long jMax = originalGalaxies.stream().mapToLong(Position::j).max().orElseThrow();

        emptyRowNrs = LongStream.range(iMin, iMax).filter(i -> originalGalaxies.stream().noneMatch(p -> p.i == i)).boxed().toList();
        emptyColNrs = LongStream.range(jMin, jMax).filter(j -> originalGalaxies.stream().noneMatch(p -> p.j == j)).boxed().toList();
    }

    @Override
    public String getAnswerPartOne() throws Exception {
        List<Position> galaxies = new ArrayList<>(originalGalaxies);
        expand(galaxies, 1L);

        return String.valueOf(StreamEx.ofCombinations(galaxies.size(), 2)
            .mapToLong(pair -> determineMinimumSteps(galaxies.get(pair[0]), galaxies.get(pair[1])))
            .sum());
    }

    @Override
    public String getAnswerPartTwo() throws Exception {
        List<Position> galaxies = new ArrayList<>(originalGalaxies);
        expand(galaxies, 999999L);

        return String.valueOf(StreamEx.ofCombinations(galaxies.size(), 2)
            .mapToLong(pair -> determineMinimumSteps(galaxies.get(pair[0]), galaxies.get(pair[1])))
            .sum());
    }

    public void expand(List<Position> galaxies, long amount) {
        emptyRowNrs.reversed().forEach(rowNr -> galaxies.replaceAll(p -> p.i > rowNr ? new Position(p.i + amount, p.j) : p));
        emptyColNrs.reversed().forEach(colNr -> galaxies.replaceAll(p -> p.j > colNr ? new Position(p.i, p.j + amount) : p));
    }

    private long determineMinimumSteps(Position start, Position end) {
        return Math.abs(end.i - start.i) + Math.abs(end.j - start.j); // Manhattan distance
    }

    record Position(long i, long j) { }
}