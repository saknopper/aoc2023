package com.github.saknopper.aoc2023.utils;

import java.util.Arrays;
import java.util.List;

public final class GridUtils {
    private GridUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> void printGrid(List<List<T>> grid, String delim) {
        grid.stream()
                .forEach(row -> System.out.println(String.join(delim, row.stream().map(String::valueOf).toList())));
    }

    public static void printGrid(int[][] grid, String delim) {
        Arrays.stream(grid).forEach(
                row -> System.out.println(String.join(delim, Arrays.stream(row).mapToObj(String::valueOf).toList())));
    }
}
