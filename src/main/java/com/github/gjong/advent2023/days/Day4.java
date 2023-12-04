package com.github.gjong.advent2023.days;

import java.util.Arrays;
import java.util.List;

class Day4 extends Executor<Long> {

    record Card(int hand, List<Integer> winningNumbers, List<Integer> yourNumbers) {
        public long score() {
            return yourNumbers.stream()
                .filter(winningNumbers::contains)
                .mapToLong(winningNumbers::indexOf)
                .count();
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

    private Card parseCard(String line) {
        // extract the card number, the winning numbers, and your numbers
        var cardNumber = Integer.parseInt(line.substring(5, 9));

        var winningNumbers = Arrays.stream(line.substring(10, line.indexOf("|"))
            .split("\s"))
            .map(Integer::parseInt)
            .toList();
        var myNumbers = Arrays.stream(line.split("\\|")[1].split("\s"))
            .map(Integer::parseInt)
            .toList();

        return new Card(
            cardNumber, 
            winningNumbers, 
            myNumbers);
    }
}