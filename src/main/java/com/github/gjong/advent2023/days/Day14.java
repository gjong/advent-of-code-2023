package com.github.gjong.advent2023.days;

import com.github.gjong.advent2023.common.CharGrid;

public class Day14 extends Executor<Long> {
    @Override
    public Long solvePart1(String input) {
        var grid = new CharGrid(input);

        var weight = 0L;
        for (var x = 0; x < grid.cols(); x++) {
            var lastSolid = 0;

            for (var y = 0; y < grid.rows(); y++) {
                var c = grid.at(x, y);
                if (c == 'O') {
                    weight += grid.rows() - (lastSolid++);
                } else if (c == '#') {
                    lastSolid = y + 1;
                }
            }
        }
        return weight;
    }

    @Override
    public Long solvePart2(String input) {
        return null;
    }

    public static void main(String[] args) {
        new Day14().execute(14);
    }

}
