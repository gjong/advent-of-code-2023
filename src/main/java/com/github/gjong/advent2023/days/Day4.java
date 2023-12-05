package com.github.gjong.advent2023.days;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

class Day4 extends Executor<Long> {

    record Card(int hand, List<Integer> winningNumbers, List<Integer> yourNumbers) {
        public long score() {
            return (long) Math.pow(2, numberMatches() - 1);
        }

        public long numberMatches() {
            return yourNumbers.stream()
                    .filter(winningNumbers::contains)
                    .count();
        }
    }

    static class CountedCards {
        private final Card card;
        private int count;

        public CountedCards(Card card) {
            this.card = card;
            this.count = 1;
        }

        public void increment(int amount) {
            this.count += amount;
        }
    }

    /**
     * The Elf leads you over to the pile of colorful cards. There, you discover dozens of scratchcards, all with their opaque covering already scratched off.
     * Picking one up, it looks like each card has two lists of numbers separated by a vertical bar (|)
     * : a list of winning numbers and then a list of numbers you have. You organize the information into a table (your puzzle input).
     * As far as the Elf has been able to figure out, you have to figure out which of the numbers you have appear in the list of winning numbers.
     * The first match makes the card worth one point and each match after the first doubles the point value of that card.
     */
    public Long solvePart1(String input) {
        return input.lines()
                .map(this::parseCard)
                .mapToLong(Card::score)
                .sum();
    }

    /**
     * Specifically, you win copies of the scratchcards below the winning card equal to the number of matches.
     * So, if card 10 were to have 5 matching numbers, you would win one copy each of cards 11, 12, 13, 14, and 15.
     */
    public Long solvePart2(String input) {
        var cardInput = input.lines()
                .map(this::parseCard)
                .map(CountedCards::new)
                .toList();

        for (var i = 0; i < cardInput.size(); i++) {
            var card = cardInput.get(i);
            if (card.card.numberMatches() > 0) {
                IntStream.range(i + 1, (int) (i + card.card.numberMatches() + 1))
                        .forEach(index -> cardInput.get(index).increment(card.count));
            }
        }

        return cardInput.stream()
                .mapToLong(card -> card.count)
                .sum();
    }

    public static void main(String[] args) {
        new Day4().execute(4);
    }

    private Card parseCard(String line) {
        var cardSplit = line.indexOf(":");
        var drawSplit = line.indexOf("|");
        // extract the card number, the winning numbers, and your numbers
        var cardNumber = Integer.parseInt(line.substring(5, cardSplit).trim());

        var winningNumbers = Arrays.stream(line.substring(cardSplit + 1, drawSplit)
                        .split("\\s"))
                .filter(number -> !number.isBlank())
                .map(nr -> Integer.parseInt(nr.trim()))
                .toList();
        var myNumbers = Arrays.stream(line.substring(drawSplit + 1).split("\\s"))
                .filter(number -> !number.isBlank())
                .map(nr -> Integer.parseInt(nr.trim()))
                .toList();

        return new Card(
                cardNumber,
                winningNumbers,
                myNumbers);
    }
}