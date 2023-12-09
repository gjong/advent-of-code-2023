package com.github.gjong.advent2023.days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day9 extends Executor<Long> {

    /**
     * To best protect the oasis, your environmental report should include a prediction of the next value in each history.
     * To do this, start by making a new sequence from the difference at each step of your history.
     * If that sequence is not all zeroes, repeat this process, using the sequence you just generated as the input sequence.
     * Once all of the values in your latest sequence are zeroes, you can extrapolate what the next value of the original history should be.
     */
    @Override
    public Long solvePart1(String input) {
        return input.lines()
                .map(this::parseLine)
                .mapToLong(this::solveRight)
                .sum();
    }

    /**
     * For each history, repeat the process of finding differences until the sequence of differences is entirely zero.
     * Then, rather than adding a zero to the end and filling in the next values of each previous sequence,
     * you should instead add a zero to the beginning of your sequence of zeroes, then fill in new first values for each
     * previous sequence.
     */
    @Override
    public Long solvePart2(String input) {
        return input.lines()
                .map(this::parseLine)
                .mapToLong(this::solveLeft)
                .sum();
    }

    public static void main(String[] args) {
        new Day9().execute(9);
    }

    private Long solveRight(List<Long> numbers) {
        if (numbers.stream().allMatch(n -> n == 0)) {
            return 0L;
        }

        return numbers.get(numbers.size() - 1) + solveRight(getIncrements(numbers));
    }

    private Long solveLeft(List<Long> numbers) {
        if (numbers.stream().allMatch(n -> n == 0)) {
            return 0L;
        }

        return numbers.get(0) - solveLeft(getIncrements(numbers));
    }

    private List<Long> getIncrements(List<Long> increments) {
        var result = new ArrayList<Long>();
        for (int i = 0; i < increments.size() - 1; i++) {
            result.add(increments.get(i + 1) - increments.get(i));
        }
        return result;
    }

    private List<Long> parseLine(String line) {
        return Arrays.stream(line.split("\\s+"))
                .map(Long::parseLong)
                .toList();
    }
}
