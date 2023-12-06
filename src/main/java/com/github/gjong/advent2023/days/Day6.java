package com.github.gjong.advent2023.days;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * As part of signing up, you get a sheet of paper (your puzzle input) that lists the time allowed for
 * each race and also the best distance ever recorded in that race.
 * To guarantee you win the grand prize, you need to make sure you go farther in each race than
 * the current record holder.
 * <p>
 * The organizer brings you over to the area where the boat races are held.
 * The boats are much smaller than you expected - they're actually toy boats, each with a big button on top.
 * Holding down the button charges the boat, and releasing the button allows the boat to move.
 * Boats move faster if their button was held longer, but time spent holding the button counts against the
 * total race time. You can only hold the button at the start of the race, and boats don't move until the
 * button is released.
 */
public class Day6 extends Executor<Long> {

    /**
     * To see how much margin of error you have, determine the number of ways you can beat the
     * record in each race; in this example, if you multiply these values together
     */
    @Override
    public Long solvePart1(String input) {
        var parsed = parseInput(input);

        var totalWaysToWin = 1L;
        for (var x = 0; x < parsed.get(0).length; x++) {
            var time = parsed.get(0)[x];
            var distance = parsed.get(1)[x];

            var waysToWin = computeWaysToWin(time, distance);
            logger.debug("Time: {}, Distance: {}, Ways to win: {}", time, distance, waysToWin);
            totalWaysToWin *= waysToWin;
        }

        return totalWaysToWin;
    }

    @Override
    public Long solvePart2(String input) {
        var lines = input.split(System.lineSeparator());

        var time = lines[0].substring("Time:".length()).replaceAll("\\s", "");
        var distance = lines[1].substring("Distance:".length()).replaceAll("\\s", "");

        // binary search may be faster
        return (long) computeWaysToWin(Long.parseLong(time), Long.parseLong(distance));
    }

    public static void main(String[] args) {
        new Day6().execute(6);
    }

    private int computeWaysToWin(long time, long distance) {
        var d = Math.sqrt(time * time - 4.0 * distance);
        var min = Math.floor(.5 * (time - d)) + 1;
        var max = Math.ceil(.5 * (time + d)) - 1;
        return (int) (max - min + 1);
    }

    private List<Integer[]> parseInput(String input) {
        var pattern = Pattern.compile("(\\d+)");

        return input.lines()
                .map(pattern::matcher)
                .map(matcher -> {
                    var numbers = new ArrayList<Integer>();
                    while (matcher.find()) {
                        numbers.add(Integer.parseInt(matcher.group(1)));
                    }
                    return numbers.toArray(new Integer[0]);
                })
                .toList();
    }
}
