package com.github.gjong.advent2023.common;

public class CharGrid {

    private final char[][] grid;

    private CharGrid(char[][] grid) {
        this.grid = grid;
    }

    public CharGrid(String input) {
        this.grid = input.lines()
                .map(String::toCharArray)
                .toArray(char[][]::new);
    }

    public char at(int x, int y) {
        return grid[y][x];
    }

    public int rows() {
        return grid.length;
    }

    public int cols() {
        return grid[0].length;
    }

    public char[] row(int idx) {
        return grid[idx];
    }

    /**
     * Transpose the grid
     */
    public CharGrid transpose() {
        char[][] rotated = new char[grid[0].length][grid.length];
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++){
                rotated[c][r] = grid[r][c];
            }
        }
        return new CharGrid(rotated);
    }
}
