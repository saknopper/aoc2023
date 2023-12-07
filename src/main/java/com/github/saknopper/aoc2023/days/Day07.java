package com.github.saknopper.aoc2023.days;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.IntStream;

public class Day07 extends Day {
    private static final URL INPUT_RESOURCE = Day03.class.getClassLoader().getResource("day07.txt");

    private static final List<Character> CARDS_AVAILABLE = List.of('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A');
    private static final List<Character> CARDS_AVAILABLE_JOKER = List.of('J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A');

    @Override
    public String getAnswerPartOne() throws Exception {
        return String.valueOf(calcTotalWinnings(false));
    }

    @Override
    public String getAnswerPartTwo() throws Exception {
        return String.valueOf(calcTotalWinnings(true));
    }

    private long calcTotalWinnings(boolean useJoker) throws Exception {
        List<HandAndBid> items = Files.lines(Paths.get(INPUT_RESOURCE.toURI())).map(l -> {
                var splitted = l.split(" ");
                return new HandAndBid(new Hand(splitted[0].trim(), determineHandType(splitted[0].trim(), useJoker)),
                        Long.valueOf(splitted[1].trim()), useJoker);
        }).sorted().toList();

        return IntStream.range(0, items.size()).mapToLong(i -> items.get(i).bid * (i + 1)).sum();
    }

    private static HandType determineHandType(String cards, boolean useJoker) {
        var cardCounts = CARDS_AVAILABLE.stream()
                .map(ca -> cards.chars().mapToObj(c -> (char) c).filter(ca::equals).count())
                .toList();

        var amountOfJokers = cards.chars().mapToObj(c -> (char) c).filter(c -> c.equals('J')).count();

        HandType type = HandType.HIGH_CARD;

        if (cardCounts.contains(5L)) {
            type = HandType.FIVE_OF_A_KIND;
        } else if (cardCounts.contains(4L)) {
            type = HandType.FOUR_OF_A_KIND;
            if (useJoker && (amountOfJokers == 1 || amountOfJokers == 4))
                type = HandType.FIVE_OF_A_KIND;
        } else if (cardCounts.contains(3L) && cardCounts.contains(2L)) {
            type = HandType.FULL_HOUSE;
            if (useJoker && (amountOfJokers == 3 || amountOfJokers == 2))
                type = HandType.FIVE_OF_A_KIND;
        } else if (cardCounts.contains(3L) && cardCounts.contains(1L)) {
            type = HandType.THREE_OF_A_KIND;
            if (useJoker && (amountOfJokers == 3 || amountOfJokers == 1))
                type = HandType.FOUR_OF_A_KIND;
        } else if (cardCounts.stream().filter(c -> c == 2L).count() == 2L) {
            type = HandType.TWO_PAIR;
            if (useJoker && amountOfJokers > 0) {
                if (amountOfJokers == 2)
                    type = HandType.FOUR_OF_A_KIND;
                else if (amountOfJokers == 1)
                    type = HandType.FULL_HOUSE;
            }
        } else if (cardCounts.contains(2L)) {
            type = HandType.ONE_PAIR;
            if (useJoker && (amountOfJokers == 1 || amountOfJokers == 2))
                type = HandType.THREE_OF_A_KIND;
        } else {
            if (useJoker && amountOfJokers == 1)
                type = HandType.ONE_PAIR;
        }

        return type;
    }

    record HandAndBid(Hand hand, long bid, boolean useJoker) implements Comparable<HandAndBid> {
        @Override
        public int compareTo(HandAndBid other) {
            if (this.hand.type == other.hand.type) {
                for (int i = 0; i < 5; i++) {
                    if (this.hand.cards.charAt(i) != other.hand.cards.charAt(i)) {
                        if (useJoker)
                            return CARDS_AVAILABLE_JOKER.indexOf(this.hand.cards.charAt(i)) - CARDS_AVAILABLE_JOKER.indexOf(other.hand.cards.charAt(i));
                        else
                            return CARDS_AVAILABLE.indexOf(this.hand.cards.charAt(i)) - CARDS_AVAILABLE.indexOf(other.hand.cards.charAt(i));
                    }
                }
            }

            return this.hand.type.compareTo(other.hand.type);
        }
    }

    record Hand(String cards, HandType type) { }

    enum HandType {
        HIGH_CARD,
        ONE_PAIR,
        TWO_PAIR,
        THREE_OF_A_KIND,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        FIVE_OF_A_KIND
    }
}