package com.github.saknopper.aoc2023.days;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.saknopper.aoc2023.utils.MathUtils;

public class Day08 extends Day {
    private static final URL INPUT_RESOURCE = Day08.class.getClassLoader().getResource("day08.txt");
    private final char[] instructions;
    private final Map<String, Directions> nodeMap;

    public Day08() {
        List<String> input;
        try (var stream = Files.lines(Paths.get(INPUT_RESOURCE.toURI()))) {
            input = stream.toList();
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("input error", e);
        }

        instructions = input.getFirst().toCharArray();
        nodeMap = getNodeMap(input.subList(2, input.size()));
    }

    @Override
    public String getAnswerPartOne() throws Exception {
        return String.valueOf(stepsNecessaryToReachEnd("AAA"));
    }

    @Override
    public String getAnswerPartTwo() throws Exception {
        var startPositions = nodeMap.keySet().stream().filter(n -> n.endsWith("A")).collect(Collectors.toCollection(ArrayList::new));
        var individualSteps = startPositions.stream().map(this::stepsNecessaryToReachEnd).toList();

        return String.valueOf(MathUtils.lcm(individualSteps.stream().mapToInt(Integer::intValue).toArray()));
    }

    private int stepsNecessaryToReachEnd(String position) {
        int steps = 0;
        for (int i = 0; true; i = (i + 1) % instructions.length) {
            if (position.endsWith("Z"))
                break;

            var instruction = instructions[i];
            var direction = nodeMap.get(position);
            position = instruction == 'L' ? direction.left : direction.right;

            steps++;
        }

        return steps;
    }

    private static Map<String, Directions> getNodeMap(List<String> input) {
        return input.stream()
            .collect(Collectors.toMap(
                line -> line.split(" = ")[0].trim(),
                line -> { 
                    var directions = line.split(" = ")[1].split(", ");
                    return new Directions(directions[0].replace("(", "").trim(),
                        directions[1].replace(")", "").trim());
                }));
    }

    record Directions(String left, String right) { }
}