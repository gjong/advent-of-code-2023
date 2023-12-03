package com.github.gjong.advent2023.days;

import java.util.*;

import static java.util.stream.Collectors.toSet;

/**
 * The engineer explains that an engine part seems to be missing from the engine,
 * but nobody can figure out which one.
 * If you can add up all the part numbers in the engine schematic,
 * it should be easy to work out which part is missing.
 * <p>
 * The engine schematic (your puzzle input) consists of a visual representation of the engine.
 * There are lots of numbers and symbols you don't really understand,
 * but apparently any number adjacent to a symbol, even diagonally,
 * is a "part number" and should be included in your sum. (Periods (.) do not count as a symbol.)
 */
public class Day3 extends Executor<Long> {

    private sealed interface Position permits NumberWithPos, SymbolPos {
    }

    record NumberWithPos(int number, int row, int startPos, int endPos) implements Position {
    }

    record SymbolPos(char c, int position) implements Position {
        boolean isGear() {
            return c == '*';
        }
    }

    record GearRatio(SymbolPos gear, NumberWithPos left, NumberWithPos right) {
    }

    private static final Set<Character> SYMBOLS = Set.of('!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+', '=', '|', '~', '`', '{', '}', '[', ']', ':', ';', '"', '\'', '<', '>', ',', '?', '/', '\\');

    /**
     * What is the sum of all of the part numbers in the engine schematic
     */
    @Override
    public Long solvePart1(String input) {

        int rowNr = 0;
        Scanner scanner = new Scanner(input);
        Queue<List<Position>> queue = new LinkedList<>();
        Set<NumberWithPos> relevantNumbers = new HashSet<>();
        while (scanner.hasNext()) {
            var currentRow = parseLine(rowNr, scanner.nextLine());
            queue.add(currentRow);

            // find the relevant numbers
            var iterationList = queue.stream().toList();
            for (var i = 0; i < iterationList.size(); i++) {
                var currentIdx = i;
                iterationList.get(i)
                        .stream()
                        .filter(pos -> pos instanceof SymbolPos)
                        .map(pos -> (SymbolPos) pos)
                        .forEach(pos -> {
                            if (currentIdx > 0) {
                                relevantNumbers.addAll(
                                        touching(iterationList.get(currentIdx - 1), pos.position()));
                            }
                            if (currentIdx < iterationList.size() - 1) {
                                relevantNumbers.addAll(
                                        touching(iterationList.get(currentIdx + 1), pos.position()));
                            }

                            relevantNumbers.addAll(
                                    touching(iterationList.get(currentIdx), pos.position()));
                        });
            }

            // keep at most 3 rows in the cache
            if (queue.size() > 3) {
                queue.poll();
            }
            rowNr++;
        }

        return relevantNumbers.stream()
                .mapToLong(NumberWithPos::number)
                .sum();
    }

    @Override
    public Long solvePart2(String input) {
        var rowNr = 0;
        var scanner = new Scanner(input);
        var queue = new LinkedList<List<Position>>();
        var gears = new HashSet<GearRatio>();
        while (scanner.hasNext()) {
            var currentRow = parseLine(rowNr, scanner.nextLine());
            queue.add(currentRow);

            // The missing part wasn't the only issue - one of the gears in the engine is wrong.
            // A gear is any * symbol that is adjacent to exactly two part numbers. Its gear
            // ratio is the result of multiplying those two numbers together.
            // This time, you need to find the gear ratio of every gear and add them all up
            // so that the engineer can figure out which gear needs to be replaced.
            var iterationList = queue.stream().toList();
            for (var i = 0; i < iterationList.size(); i++) {
                var currentIdx = i;
                iterationList.get(i)
                        .stream()
                        .filter(pos -> pos instanceof SymbolPos)
                        .map(SymbolPos.class::cast)
                        .filter(SymbolPos::isGear)
                        .forEach(pos -> {
                            var touching = new ArrayList<>(touching(iterationList.get(currentIdx), pos.position()));
                            if (currentIdx < iterationList.size() - 1) {
                                touching.addAll(touching(iterationList.get(currentIdx + 1), pos.position()));
                            }
                            if (currentIdx > 0) {
                                touching.addAll(touching(iterationList.get(currentIdx - 1), pos.position()));
                            }

                            if (touching.size() == 2) {
                                gears.add(
                                        new GearRatio(pos, touching.get(0), touching.get(1)));
                            }
                        });
            }

            if (queue.size() > 3) {
                queue.poll();
            }

            rowNr++;
        }

        return gears.stream()
                .mapToLong(gear -> (long) gear.left().number() * gear.right().number())
                .sum();
    }

    public static void main(String[] args) {
        new Day3().execute(3);
    }

    private Set<NumberWithPos> touching(List<Position> inputRow, int position) {
        return inputRow.stream()
                .filter(pos -> pos instanceof NumberWithPos)
                .map(NumberWithPos.class::cast)
                .filter(pos -> pos.endPos() >= position && pos.startPos() <= position)
                .collect(toSet());
    }

    private List<Position> parseLine(int rowNr, String line) {
        var currentRow = new ArrayList<Position>();

        var digit = "";
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (Character.isDigit(c)) {
                digit += c;
            } else {
                if (!digit.isEmpty()) {
                    currentRow.add(new NumberWithPos(
                            Integer.parseInt(digit),
                            rowNr,
                            Math.max(0, i - digit.length() - 1),
                            i));
                    digit = "";
                }
                if (SYMBOLS.contains(c)) {
                    currentRow.add(new SymbolPos(c, i));
                }
            }
        }

        if (!digit.isEmpty()) {
            currentRow.add(new NumberWithPos(
                    Integer.parseInt(digit),
                    rowNr,
                    Math.max(0, line.length() - digit.length() - 1),
                    line.length()));
        }

        return currentRow;
    }
}
