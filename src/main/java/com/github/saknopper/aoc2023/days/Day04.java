package com.github.saknopper.aoc2023.days;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day04 extends Day {
    private static final URL INPUT_RESOURCE = Day03.class.getClassLoader().getResource("day04.txt");
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");

    @Override
    public String getAnswerPartOne() throws Exception {
        var scoresSum = Files.lines(Paths.get(INPUT_RESOURCE.toURI()))
                .map(Day04::convertToCard)
                .mapToLong(Card::amountOfWinningNumbers)
                .map(amount -> amount == 0L ? 0L : (long) Math.pow(2, amount - (double) 1))
                .sum();

        return String.valueOf(scoresSum);
    }

    @Override
    public String getAnswerPartTwo() throws Exception {
        var cards = Files.lines(Paths.get(INPUT_RESOURCE.toURI()))
                .map(Day04::convertToCard).collect(Collectors.toCollection(ArrayList::new));

        for (int i = 0; i < cards.size(); i++) {
            var curCard = cards.get(i);
            var winningNrs = curCard.amountOfWinningNumbers();
            for (int j = i + 1; j < i + 1 + winningNrs; j++) {
                var nextCard = cards.get(j);
                cards.set(j, new Card(nextCard.id, nextCard.amount + curCard.amount, nextCard.winningNrs, nextCard.ourNrs));
            }
        }

        return String.valueOf(cards.stream().mapToLong(Card::amount).sum());
    }

    private static Card convertToCard(String input) {
        var splitted = input.split(": ");
        var cardId = Long.valueOf(splitted[0].replace("Card", "").trim());
        var numbers = splitted[1].split(Pattern.quote(" | "));
        var winningNrs = WHITESPACE_PATTERN.splitAsStream(numbers[0].trim()).map(Long::valueOf).toList();
        var ourNrs = WHITESPACE_PATTERN.splitAsStream(numbers[1].trim()).map(Long::valueOf).toList();

        return new Card(cardId, 1L, winningNrs, ourNrs);
    }

    public record Card(long id, long amount, List<Long> winningNrs, List<Long> ourNrs) {
        public long amountOfWinningNumbers() {
            return ourNrs.stream().filter(winningNrs::contains).count();
        }
     }
}