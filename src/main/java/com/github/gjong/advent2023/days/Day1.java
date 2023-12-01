package com.github.gjong.advent2023.days;

import java.util.HashMap;
import java.util.Optional;

public class Day1 extends Executor<Integer> {

    @Override
    public Integer solvePart1(String input) {
        return input.lines()
                .map(this::getBoundInts)
                .mapToInt(Integer::parseInt)
                .sum();
    }

    @Override
    public Integer solvePart2(String input) {
        return input.lines()
                .map(this::parseLine)
                .mapToInt(Integer::parseInt)
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
        var map = new HashMap<String, String>();
        map.put("1", "1");
        map.put("2", "2");
        map.put("3", "3");
        map.put("4", "4");
        map.put("5", "5");
        map.put("6", "6");
        map.put("7", "7");
        map.put("8", "8");
        map.put("9", "9");
        map.put("one", "1");
        map.put("two", "2");
        map.put("three", "3");
        map.put("four", "4");
        map.put("five", "5");
        map.put("six", "6");
        map.put("seven", "7");
        map.put("eight", "8");
        map.put("nine", "9");

        var firstNumber = Optional.<String>empty();
        var lastNumber = "";

        var parsing = line;
        while (!parsing.isEmpty()) {
            for (var entry : map.entrySet()) {
                if (parsing.startsWith(entry.getKey())) {
                    if (firstNumber.isEmpty()) {
                        firstNumber = Optional.of(entry.getValue());
                    } else {
                        lastNumber = entry.getValue();
                    }
                    break;
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
