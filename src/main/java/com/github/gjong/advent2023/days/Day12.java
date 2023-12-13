package com.github.gjong.advent2023.days;

import com.jongsoft.lang.API;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Day12 extends Executor<Long> {
    private final Map<String, Long> cache = new HashMap<>();

    @Override
    public Long solvePart1(String input) {
        return input.lines()
                .map(s -> s.split("\\s"))
                .mapToLong(s -> compute(s[0] + ".", parseRuns(s[1])))
                .sum();
    }

    @Override
    public Long solvePart2(String input) {
        return input.lines()
                .map(s -> s.split("\\s"))
                .map(s -> API.Tuple(repeat(s[0], "?"), repeat(s[1], ",")))
                .mapToLong(s -> compute(s.getFirst() + ".", parseRuns(s.getSecond())))
                .sum();
    }

    private String repeat(String input, String joining) {
        return String.join(joining, Collections.nCopies(5, input));
    }

    public static void main(String[] args) {
        new Day12().execute(12);
    }

    private int[] parseRuns(String line) {
        return Arrays.stream(line.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    private long compute(String springs, int[] expectedSprings) {
        cache.clear();
        return compute(springs, expectedSprings, 0, 0);
    }

    private long compute(String springs, int[] expectedSprings, int charIdx, int group) {
        if (group == expectedSprings.length) {
            return !springs.substring(charIdx).contains("#") ? 1 : 0;
        }

        var expected = expectedSprings[group];
        if (springs.length() - charIdx < expected + 1) {
            return 0;
        }

        var key = charIdx + "," + group;
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        var res = 0L;
        if (springs.charAt(charIdx) != '#') {
            res += compute(springs, expectedSprings, charIdx + 1, group);
        }
        if (!springs.substring(charIdx, charIdx + expected).contains(".") && springs.charAt(charIdx + expected) != '#') {
            res += compute(springs, expectedSprings, charIdx + expected + 1, group + 1);
        }

        cache.put(key, res);
        return res;
    }
}