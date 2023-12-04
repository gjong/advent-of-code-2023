package com.github.gjong.advent2023.days;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

class Day4 extends Executor<Long> {

    record Card(int hand, List<Integer> winningNumbers, List<Integer> yourNumbers) {
        public long score() {
            var nrCardsMatch = yourNumbers.stream()
                    .filter(winningNumbers::contains)
                    .mapToLong(winningNumbers::indexOf)
                    .count();

            return (long) Math.pow(2, nrCardsMatch - 1);
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

    public Long solvePart2(String input) {
        return 0L;
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