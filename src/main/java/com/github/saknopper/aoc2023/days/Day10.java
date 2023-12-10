package com.github.saknopper.aoc2023.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day10 extends Day {
    private static final URL INPUT_RESOURCE = Day10.class.getClassLoader().getResource("day10.txt");

    private final List<ArrayList<Tile>> tiles;
    private final Position startPosition;

    public Day10() {
        try (var stream = Files.lines(Paths.get(INPUT_RESOURCE.toURI()))) {
            tiles = stream
                    .map(l -> l.chars().mapToObj(c -> (char) c).map(c -> new Tile(TileType.fromCharacter(c)))
                            .collect(Collectors.toCollection(ArrayList::new)))
                    .toList();
            Position p = null;
            for (int i = 0; i < tiles.size(); i++) {
                for (int j = 0; j < tiles.get(0).size(); j++) {
                    if (tiles.get(i).get(j).type == TileType.START) {
                        p = new Position(i, j);
                        break;
                    }
                }
            }

            startPosition = p;
            tiles.get(startPosition.i).set(startPosition.j, new Tile(determineStartPositionType()));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("input error", e);
        }
    }

    @Override
    public String getAnswerPartOne() throws Exception {
        return String.valueOf(calculatePaths().get(0).size() - 1);
    }

    @Override
    public String getAnswerPartTwo() throws Exception {
        var path = calculatePaths().stream().flatMap(List::stream).collect(Collectors.toSet());

        // Slight variation of "Even-odd rule" (diagonal ray, check for outside corners)
        long amountInside = 0L;
        int height = tiles.size();
        int width = tiles.get(0).size();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (path.contains(new Position(i, j)))
                    continue;

                int intersections = 0;
                for (int i2 = i, j2 = j; i2 < height && j2 < width; i2++, j2++) {
                    if (path.contains(new Position(i2, j2))) {
                        var tile = tiles.get(i2).get(j2);
                        if (tile.type != TileType.NE && tile.type != TileType.SW)
                            intersections++;
                    }
                }

                if (intersections % 2 == 1)
                    amountInside++;
            }
        }

        return String.valueOf(amountInside);
    }

    private List<List<Position>> calculatePaths() {
        List<Position> path1 = new ArrayList<>();
        path1.add(startPosition);
        List<Position> path2 = new ArrayList<>();
        path2.add(startPosition);

        var paths = List.of(path1, path2);

        var nextPositions = possibleNextPositions(startPosition);
        path1.add(nextPositions.get(0));
        path2.add(nextPositions.get(1));

        while (true) {
            for (var path : paths) {
                var nextPossiblePositions = possibleNextPositions(path.getLast());
                var nextPosition = nextPossiblePositions.stream().filter(p -> !path.get(path.size() - 2).equals(p)).findFirst().orElseThrow();
                path.add(nextPosition);
            }

            if (paths.stream().map(List::getLast).distinct().count() == 1L)
                break;
        }

        return paths;
    }

    private List<Position> possibleNextPositions(Position p) {
        switch (tiles.get(p.i).get(p.j).type) {
            case TileType.NS -> { return List.of(new Position(p.i - 1, p.j), new Position(p.i + 1, p.j)); }
            case TileType.EW -> { return List.of(new Position(p.i, p.j - 1), new Position(p.i, p.j + 1)); }
            case TileType.NE -> { return List.of(new Position(p.i - 1, p.j), new Position(p.i, p.j + 1)); }
            case TileType.NW -> { return List.of(new Position(p.i - 1, p.j), new Position(p.i, p.j - 1)); }
            case TileType.SW -> { return List.of(new Position(p.i + 1, p.j), new Position(p.i, p.j - 1)); }
            case TileType.SE -> { return List.of(new Position(p.i + 1, p.j), new Position(p.i, p.j + 1)); }
            default -> { return List.of(p); }
        }
    }

    private TileType determineStartPositionType() {
        boolean toNorth = false;
        boolean toSouth = false;
        boolean toEast = false;
        boolean toWest = false;

        try {
            TileType north = tiles.get(startPosition.i - 1).get(startPosition.j).type;
            if (List.of(TileType.NS, TileType.SE, TileType.SW).contains(north))
                toNorth = true;
        } catch (Exception e) { /* ignore */}
        try {
            TileType south = tiles.get(startPosition.i + 1).get(startPosition.j).type;
            if (List.of(TileType.NS, TileType.NE, TileType.NW).contains(south))
                toSouth = true;
        } catch (Exception e) { /* ignore */ }
        try {
            TileType east = tiles.get(startPosition.i).get(startPosition.j + 1).type;
            if (List.of(TileType.EW, TileType.NW, TileType.SW).contains(east))
                toEast = true;
        } catch (Exception e) { /* ignore */ }
        try { 
            TileType west = tiles.get(startPosition.i).get(startPosition.j - 1).type;
            if (List.of(TileType.EW, TileType.NE, TileType.SE).contains(west))
                toWest = true;
        } catch (Exception e) { /* ingore */ }

        if (toNorth && toSouth)
            return TileType.NS;
        if (toNorth && toEast)
            return TileType.NE;
        if (toNorth && toWest)
            return TileType.NW;
        if (toSouth && toEast)
            return TileType.SE;
        if (toSouth && toWest)
            return TileType.SW;
        if (toEast && toWest)
            return TileType.EW;

        throw new RuntimeException("Unhandled starting position");
    }

    record Tile(TileType type) { }

    enum TileType {
        NS('|'),
        EW('-'),
        NE('L'),
        NW('J'),
        SW('7'),
        SE('F'),
        GROUND('.'),
        START('S');

        private final char character;

        TileType(char character) {
            this.character = character;
        }

        public char getCharacter() {
            return this.character;
        }

        public static TileType fromCharacter(char character) {
            return Stream.of(TileType.values()).filter(t -> t.getCharacter() == character).findFirst().orElseThrow();
        }
    }

    record Position(int i, int j) { }
}