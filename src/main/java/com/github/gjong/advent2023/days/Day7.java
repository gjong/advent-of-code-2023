package com.github.gjong.advent2023.days;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * In Camel Cards, you get a list of hands, and your goal is to order them based on the strength of each hand.
 * A hand consists of five cards labeled one of A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, or 2.
 * The relative strength of each card follows this order, where A is the highest and 2 is the lowest.
 * <p>
 * Every hand is exactly one type. From strongest to weakest, they are:
 * <ul>
 *  <li>Five of a kind, where all five cards have the same label: AAAAA</li>
 *  <li>Four of a kind, where four cards have the same label and one card has a different label: AA8AA</li>
 *  <li>Full house, where three cards have the same label, and the remaining two cards share a different label: 23332</li>
 *  <li>Three of a kind, where three cards have the same label, and the remaining two cards are each different from any other card in the hand: TTT98</li>
 *  <li>Two pair, where two cards share one label, two other cards share a second label, and the remaining card has a third label: 23432</li>
 *  <li>One pair, where two cards share one label, and the other three cards have a different label from the pair and each other: A23A4</li>
 *  <li>High card, where all cards' labels are distinct: 23456</li>
 * </ul>
 * <p>
 * Hands are primarily ordered based on type; for example, every full house is stronger than any three of a kind.
 * <p>
 * If two hands have the same type, a second ordering rule takes effect.
 * Start by comparing the first card in each hand. If these cards are different,
 * the hand with the stronger first card is considered stronger.
 * If the first card in each hand have the same label, however,
 * then move on to considering the second card in each hand.
 * If they differ, the hand with the higher second card wins;
 * otherwise, continue with the third card in each hand, then the fourth, then the fifth.
 */
public class Day7 extends Executor<Long> {

    enum HandResult {
        FIVE_OF_A_KIND(7),
        FOUR_OF_A_KIND(6),
        FULL_HOUSE(5),
        THREE_OF_A_KIND(4),
        TWO_PAIR(3),
        ONE_PAIR(2),
        HIGH_CARD(1);
        private final int score;

        HandResult(int score) {
            this.score = score;
        }
    }

    static class Card implements Comparable<Card> {
        private final int strength;

        Card(Character card) {
            this.strength = switch(card) {
                case 'A' -> 14;
                case 'K' -> 13;
                case 'Q' -> 12;
                case 'J' -> 11;
                case 'T' -> 10;
                case 'Z' -> 1;
                default -> Integer.parseInt(String.valueOf(card));
            };
        }

        @Override
        public int compareTo(Card o) {
            return this.strength - o.strength;
        }
    }

    record Move(Hand hand, int bed) {
    }

    record Hand(String cards, int score) implements Comparable<Hand> {
        @Override
        public int compareTo(Hand o) {
            for (var i = 0; i < 5; i++) {
                var thisCard = new Card(cards.charAt(i));
                var otherCard = new Card(o.cards.charAt(i));

                if (thisCard.compareTo(otherCard) != 0) {
                    return thisCard.compareTo(otherCard);
                }
            }
            return 0;
        }
    }

    /**
     * Each hand wins an amount equal to its bid multiplied by its rank, where the weakest hand gets rank 1,
     * the second-weakest hand gets rank 2, and so on up to the strongest hand.
     * Because there are five hands in this example, the strongest hand will have rank 5 and its bid will
     * be multiplied by 5.
     */
    @Override
    public Long solvePart1(String input) {
        var list = input.lines()
                .map(line -> readLine(line, this::computeNormalHand))
                .collect(Collectors.groupingBy(m -> m.hand().score(), Collectors.toList()));

        return computeScore(list);
    }

    @Override
    public Long solvePart2(String input) {
        var list = input.lines()
                .map(line -> readLine(line, this::computeJokerHand))
                .map(move -> new Move(
                        new Hand(
                                move.hand.cards.replaceAll("J", "Z"),
                                move.hand.score),
                        move.bed()))
                .collect(Collectors.groupingBy(m -> m.hand().score(), Collectors.toList()));

        return computeScore(list);
    }

    public static void main(String[] args) {
        new Day7().execute(7);
    }

    private long computeScore(Map<Integer, List<Move>> list) {
        var score = 0L;
        var rank = 1;
        for (var entry : list.entrySet()) {
            if (entry.getValue().size() == 1) {
                score += (long) entry.getValue().get(0).bed() * rank;
                rank++;
            } else {
                var sorted = entry.getValue()
                        .stream()
                        .sorted(Comparator.comparing(Move::hand))
                        .toList();
                for (var move : sorted) {
                    score += (long) move.bed() * rank;
                    rank++;
                }
            }
        }
        return score;
    }

    private int computeNormalHand(String hand) {
        var groupedHand = Arrays.stream(hand.split(""))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        var handScore = HandResult.HIGH_CARD;
        for (var count : groupedHand.values()) {
            handScore = switch (count.intValue()) {
                case 5 -> HandResult.FIVE_OF_A_KIND;
                case 4 -> HandResult.FOUR_OF_A_KIND;
                case 3 -> handScore == HandResult.ONE_PAIR
                        ? HandResult.FULL_HOUSE
                        : HandResult.THREE_OF_A_KIND;
                case 2 -> handScore == HandResult.ONE_PAIR
                        ? HandResult.TWO_PAIR
                        : handScore == HandResult.THREE_OF_A_KIND
                        ? HandResult.FULL_HOUSE : HandResult.ONE_PAIR;
                default -> handScore;
            };
        }
        return handScore.score;
    }

    private int computeJokerHand(String hand) {
        var handWithoutJokers = hand.replaceAll("J", "");
        var handScore = computeNormalHand(handWithoutJokers);
        var nrJokers = hand.length() - handWithoutJokers.length();

        if (nrJokers > 0) {
            handScore = switch (handScore) {
                case 6 -> HandResult.FIVE_OF_A_KIND.score;      // four of a kind with joker
                case 5 -> throw new IllegalStateException("Should not happen");
                case 4 -> 4 + 1 + nrJokers;                     // three of a kind with joker
                case 3 -> HandResult.FULL_HOUSE.score;          // two pair with joker
                case 2 -> nrJokers == 1
                        ? HandResult.THREE_OF_A_KIND.score
                        : nrJokers == 2
                            ? HandResult.FOUR_OF_A_KIND.score
                            : HandResult.FIVE_OF_A_KIND.score;
                case 1 -> nrJokers == 1
                        ? HandResult.ONE_PAIR.score
                        : nrJokers == 2
                            ? HandResult.TWO_PAIR.score
                            : nrJokers == 3
                                ? HandResult.THREE_OF_A_KIND.score
                                : nrJokers == 4
                                    ? HandResult.FOUR_OF_A_KIND.score
                                    : HandResult.FIVE_OF_A_KIND.score;
                default -> throw new IllegalStateException("Should not happen");
            };
        }

        return handScore;
    }

    private Move readLine(String line, Function<String, Integer> handRanker) {
        var hand = line.substring(0, 5);
        var bed = Integer.parseInt(line.substring(6));

        return new Move(new Hand(hand, handRanker.apply(hand)), bed);
    }
}
