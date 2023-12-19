package com.github.gjong.advent2023.days;

import com.github.gjong.advent2023.common.CharGrid;
import com.github.gjong.advent2023.common.geometry.Point;

import java.util.*;

public class Day17 extends Executor<Long> {

    record PathCost(Point location, char direction, int cost) implements Comparable<PathCost> {
        @Override
        public int compareTo(PathCost o) {
            return cost - o.cost;
        }

        public int hashCode() {
            return Objects.hash(location, direction);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof PathCost other) {
                return location.equals(other.location) && direction == other.direction;
            }
            return false;
        }

        public List<PathCost> nextSteps(CharGrid grid, int minSteps, int maxSteps) {
            var options = new ArrayList<PathCost>();

            var currentTranslation = Point.directionFromChar(direction);
            for (char updated : "NSWE".toCharArray()) {
                var translation = Point.directionFromChar(updated);
                if (translation.equals(currentTranslation) ||
                        translation.inverse().equals(currentTranslation)) {
                    // don't go back the way we came
                    continue;
                }

                var updatedCost = cost;
                var updatedLocation = location;
                for (int i = 1; i <= maxSteps; ++i) {
                    updatedLocation = updatedLocation.translate(translation);
                    if (grid.bounds().inBounds(updatedLocation)) {
                        updatedCost += grid.at(updatedLocation) - '0';

                        if (i >= minSteps) {
                            options.add(new PathCost(updatedLocation, updated, updatedCost));
                        }
                    } else {
                        // out of bounds
                        break;
                    }
                }
            }

            return options;
        }
    }

    @Override
    public Long solvePart1(String input) {
        return (long) solve(input, 1, 3);

    }

    @Override
    public Long solvePart2(String input) {
        return (long) solve(input, 4, 10);
    }

    private int solve(String input, int minSteps, int maxSteps) {
        var grid = new CharGrid(input);
        var end = new Point(grid.cols() - 1, grid.rows() - 1);

        var visited = new HashMap<PathCost, Integer>();
        visited.put(new PathCost(new Point(0, 0), '.', 0), 0);

        var queue = new PriorityQueue<>(visited.keySet());
        while (!queue.isEmpty()) {
            var current = queue.poll();
            if (current.location().equals(end)) {
                return current.cost();
            }

            for (var next : current.nextSteps(grid, minSteps,  maxSteps)) {
                if (next.cost() < visited.getOrDefault(next, Integer.MAX_VALUE)) {
                    visited.put(next, next.cost());
                    queue.add(next);
                }
            }
        }

        throw new IllegalStateException("No path found");
    }

    public static void main(String[] args) {
        new Day17().execute(17);
    }
}
