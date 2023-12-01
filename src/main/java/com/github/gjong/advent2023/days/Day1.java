package com.github.gjong.advent2023.days;

import java.util.Map;
import java.util.Optional;

public class Day1 extends Executor<Integer> {

    @Override
    public Integer solvePart1(String input) {
        return input.lines()
                .map(this::getBoundInts)
                .map(Integer::parseInt)
                .mapToInt(Integer::intValue)
                .sum();
    }

    @Override
    public Integer solvePart2(String input) {
        return input.lines()
                .map(this::parseLine)
                .map(Integer::parseInt)
                .mapToInt(Integer::intValue)
                .sum();
    }

    private String getBoundInts(String line) {
        var numbers = line.chars()
                .filter(Character::isDigit)
                .mapToObj(Character::toString)
                .toList();

        return numbers.get(0) + numbers.get(numbers.size() - 1);
    }

    private String parseLine(String line) {
        var map = Map.of(
                "one", "1",
                "two", "2",
                "three", "3",
                "four", "4",
                "five", "5",
                "six", "6",
                "seven", "7",
                "eight", "8",
                "nine", "9");

        var firstNumber = Optional.<String>empty();
        var lastNumber = "";

        var parsing = line;
        while (!parsing.isEmpty()) {
            var c = parsing.charAt(0);

            if (Character.isDigit(c)) {
                if (firstNumber.isEmpty()) {
                    firstNumber = Optional.of(Character.toString(c));
                } else {
                    lastNumber = Character.toString(c);
                }
            } else {
                var maxWord = parsing.substring(0, Math.min(5, parsing.length()));
                for (var entry : map.entrySet()) {
                    if (maxWord.startsWith(entry.getKey())) {
                        if (firstNumber.isEmpty()) {
                            firstNumber = Optional.of(entry.getValue());
                        } else {
                            lastNumber = entry.getValue();
                        }
                        break;
                    }
                }
            }

            parsing = parsing.substring(1);
        }

        if (lastNumber.isBlank()) {
            lastNumber = firstNumber.get();
        }

        return firstNumber.get() + lastNumber;
    }

    public static void main(String[] args) {
        var day1 = new Day1();
        day1.execute(1);
    }

}
