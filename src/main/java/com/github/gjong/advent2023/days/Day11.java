package com.github.gjong.advent2023.days;

import com.github.gjong.advent2023.common.geometry.Point;

import java.util.ArrayList;
import java.util.List;

public class Day11 extends Executor<Long> {

    @Override
    public Long solvePart1(String input) {
        return computeWithFactor(input, 1);
    }

    @Override
    public Long solvePart2(String input) {
        return computeWithFactor(input, 1000000);
    }

    public static void main(String[] args) {
        new Day11().execute(11);
    }

    public long computeWithFactor(String input, int factor) {
        var map = parseGalaxyMap(input);
        var universes = expandUniverse(map, factor);
        return computeDistances(universes);
    }

    private long computeDistances(List<Point> universes) {
        var distances = new ArrayList<Integer>();
        for (int i = 0; i < universes.size(); i++) {
            for (int j = i + 1; j < universes.size(); j++) {
                var p1 = universes.get(i);
                var p2 = universes.get(j);
                var x = Math.abs(p1.x() - p2.x());
                var y = Math.abs(p1.y() - p2.y());
                distances.add(x + y);
            }
        }

        return distances.stream().mapToLong(i -> i).sum();
    }

    /**
     * Due to something involving gravitational effects, only some space expands.
     * In fact, the result is that any rows or columns that contain no galaxies should all actually be twice as big.
     */
    private List<Point> expandUniverse(char[][] original, int factor) {
        List<Point> universes = new ArrayList<>();

        var emptyRowIndices = new ArrayList<Integer>();
        var emptyColIndices = new ArrayList<Integer>();

        // find empty rows
        for (int y = 0; y < original.length; y++) {
            var row = original[y];
            for (int x = 0; x < row.length; x++) {
                if (row[x] == '#') {
                    break;
                }

                if (x == row.length - 1) {
                    emptyRowIndices.add(y);
                }
            }
        }

        // find empty columns
        for (int x = 0; x < original[0].length; x++) {
            for (int y = 0; y < original.length; y++) {
                if (original[y][x] == '#') {
                    break;
                }

                if (y == original.length - 1) {
                    emptyColIndices.add(x);
                }
            }
        }

        // convert to list of universe coordinates
        for (int y = 0; y < original.length; y++) {
            for (int x = 0; x < original[0].length; x++) {
                if (original[y][x] == '#') {
                    var effX = x; // effective final x
                    var effY = y; // effective final y

                    // expand the coordinates using the empty rows and columns
                    var offsetX = (int) emptyColIndices.stream().filter(i -> i < effX).count();
                    var offsetY = (int) emptyRowIndices.stream().filter(i -> i < effY).count();
                    if (factor > 1) {
                        offsetX = offsetX * factor - offsetX;
                        offsetY = offsetY * factor - offsetY;
                    }

                    universes.add(new Point(x + offsetX, y + offsetY));
                }
            }
        }

        return universes;
    }

    /**
     * The researcher has collected a bunch of data and compiled the data into a single giant image (your puzzle input).
     * The image includes empty space (.) and galaxies (#).
     */
    private char[][] parseGalaxyMap(String input) {
        String[] lines = input.lines().toArray(String[]::new);

        // non expanded universe
        char[][] map = new char[lines.length][lines[0].length()];
        for (int y = 0; y < lines.length; y++) {
            map[y] = lines[y].toCharArray();
        }

        return map;
    }
}
