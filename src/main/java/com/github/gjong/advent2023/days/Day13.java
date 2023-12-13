package com.github.gjong.advent2023.days;


import com.github.gjong.advent2023.common.CharGrid;

import java.util.List;
import java.util.stream.Stream;

public class Day13 extends Executor<Long> {
    private static final int FACTOR_ROWS = 100;

    @Override
    public Long solvePart1(String input) {
        return solve(parseInput(input), 0);
    }

    @Override
    public Long solvePart2(String input) {
        return solve(parseInput(input), 1);
    }

    public static void main(String[] args) {
        new Day13().execute(13);
    }

    public long solve(List<CharGrid> grids, int errors) {
        int answer = 0;
        for (var grid : grids) {
            answer += calculateReflections(grid, errors, FACTOR_ROWS);
            answer += calculateReflections(grid.transpose(), errors, 1);
        }
        return answer;
    }

    private int calculateReflections(CharGrid grid, int errors, int factor) {
        int result = 0;
        for (var i = 1; i < grid.rows(); i++) {
            if (checkReflections(grid, i, errors)) {
                result += factor * i;
            }
        }
        return result;
    }

    private boolean checkReflections(CharGrid pattern, int position, int allowedErrors) {
        var errors = 0;
        for (int i = position - 1, j = position; i >= 0 && j < pattern.rows(); i--, j++) {
            for (var column = 0; column < pattern.cols(); column++) {
                if (pattern.row(j)[column] != pattern.row(i)[column] && ++errors > allowedErrors) {
                    return false;
                }
            }
        }
        return errors == allowedErrors;
    }

    private List<CharGrid> parseInput(String input) {
        return Stream.of(input.split("\n\n"))
                .map(CharGrid::new)
                .toList();
    }
}
