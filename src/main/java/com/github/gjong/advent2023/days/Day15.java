package com.github.gjong.advent2023.days;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Day15 extends Executor<Long> {

    /**
     * The HASH algorithm is a way to turn any string of characters into a single number in the range 0 to 255.
     * To run the HASH algorithm on a string, start with a current value of 0.
     * Then, for each character in the string starting from the beginning:
     *
     * <li>Determine the ASCII code for the current character of the string.</li>
     * <li>Increase the current value by the ASCII code you just determined.</li>
     * <li>Set the current value to itself multiplied by 17.</li>
     * <li>Set the current value to the remainder of dividing itself by 256.</li>
     * <li>After following these steps for each character in the string in order, the current value is the output of the HASH algorithm.</li>
     */
    @Override
    public Long solvePart1(String input) {
        return Stream.of(input.split(","))
                .mapToLong(this::hash)
                .sum();
    }

    @Override
    public Long solvePart2(String input) {
        var boxes = new HashMap<Long, List<Lens>>(255);
        IntStream.range(0, 256).forEach(i -> boxes.put((long) i, new ArrayList<>()));

        for (var instruction : input.split(",")) {
            if (instruction.endsWith("-")) {
                var lens = instruction.substring(0, instruction.length() - 1);
                var hash = hash(lens);
                boxes.get(hash).removeIf(l -> l.lens.equals(lens));
                continue;
            }

            var lens = new Lens(instruction.split("=")[0], Integer.parseInt(instruction.split("=")[1]));
            var box = hash(lens.lens);
            var lenses = boxes.get(box);

            // find index to replace
            var replaceIndex = -1;
            for (int i = 0; i < lenses.size(); i++) {
                if (lenses.get(i).lens.equals(lens.lens)) {
                    replaceIndex = i;
                    break;
                }
            }

            if (replaceIndex != -1) {
                lenses.remove(replaceIndex);
                lenses.add(replaceIndex, lens);
            } else {
                // add lens to end
               lenses.add(lens);
            }
        }

        return boxes.entrySet()
                .stream()
                .filter(e -> !e.getValue().isEmpty())
                .flatMapToLong(e -> computeStrength(e.getKey(), e.getValue()))
                .sum();
    }

    record Lens(String lens, int focalStrength) {}

    public static void main(String[] args) {
        new Day15().execute(15);
    }

    private LongStream computeStrength(long box, List<Lens> lenses) {
        var strengths = new ArrayList<Long>();
        for (int i = 0; i < lenses.size(); i++) {
            var lens = lenses.get(i);
            strengths.add((box + 1) * (i + 1) * lens.focalStrength);
        }
        return strengths.stream().mapToLong(Long::longValue);
    }

    private long hash(String input) {
        long hash = 0;
        for (char c : input.toCharArray()) {
            hash += c;
            hash *= 17;
            hash %= 256;
        }
        return hash;
    }
}
