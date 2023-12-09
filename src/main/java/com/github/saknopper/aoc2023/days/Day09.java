package com.github.saknopper.aoc2023.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import one.util.streamex.StreamEx;

public class Day09 extends Day {
    private static final URL INPUT_RESOURCE = Day09.class.getClassLoader().getResource("day09.txt");

    private final List<List<Long>> input;

    public Day09() {
        try (var stream = Files.lines(Paths.get(INPUT_RESOURCE.toURI()))) {
            input = stream.map(l -> Stream.of(l.split(" ")).map(Long::valueOf).toList()).toList();
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("input error", e);
        }
    }

    @Override
    public String getAnswerPartOne() throws Exception {
        return String.valueOf(input.stream().mapToLong(list -> extrapolate(list, false, Day09::forward)).sum());
    }

    @Override
    public String getAnswerPartTwo() throws Exception {
        return String.valueOf(input.stream().mapToLong(list -> extrapolate(list, true, Day09::backward)).sum());
    }

    private static long extrapolate(List<Long> input, boolean backward, BiConsumer<List<List<Long>>, Integer> operation) {
        List<List<Long>> sequences = new ArrayList<>();
        sequences.add(new ArrayList<>(input));

        while (!sequences.getLast().stream().allMatch(Long.valueOf(0L)::equals))
            sequences.add(StreamEx.ofSubLists(sequences.getLast(), 2, 1).map(pair -> pair.getLast() - pair.getFirst()).toList());

        sequences.getLast().add(0L);
        for (int i = sequences.size() - 2; i >= 0; i--)
            operation.accept(sequences, i);

        if (backward)
            return sequences.getFirst().getFirst();

        return sequences.getFirst().getLast();
    }

    private static void forward(List<List<Long>> sequences, int i) {
        sequences.get(i).add(sequences.get(i).getLast() + sequences.get(i + 1).getLast());
    }

    private static void backward(List<List<Long>> sequences, int i) {
        sequences.get(i).addFirst(sequences.get(i).getFirst() - sequences.get(i + 1).getFirst());
    }
}