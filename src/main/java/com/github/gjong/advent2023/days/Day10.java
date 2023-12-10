package com.github.gjong.advent2023.days;

import com.github.gjong.advent2023.common.geometry.Point;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Scanning the area, you discover that the entire field you're standing on is densely packed with pipes; it was hard to tell at first because they're the same metallic silver color as the "ground". You make a quick sketch of all of the surface pipes you can see (your puzzle input).
 * <p>
 * The pipes are arranged in a two-dimensional grid of tiles:
 * <ul>
 * <li>| is a vertical pipe connecting north and south.</li>
 * <li>- is a horizontal pipe connecting east and west.</li>
 * <li>L is a 90-degree bend connecting north and east.</li>
 * <li>J is a 90-degree bend connecting north and west.</li>
 * <li>7 is a 90-degree bend connecting south and west.</li>
 * <li>F is a 90-degree bend connecting south and east.</li>
 * <li>. is ground; there is no pipe in this tile.</li>
 * <li>S is the starting position of the animal; there is a pipe on this tile, but your sketch doesn't show what shape the pipe has.</li>
 * </ul>
 * <p>
 * Based on the acoustics of the animal's scurrying, you're confident the pipe that contains the animal is one large, continuous loop.
 */
public class Day10 extends Executor<Long> {

    record Grid(Point start, Map<Point, List<Point>> graph) {
    }

    record Path(Point start, int length) {
    }

    /**
     * Find the single giant loop starting at S.
     * How many steps along the loop does it take to get from the starting position to the point farthest from the starting position?
     */
    @Override
    public Long solvePart1(String input) {
        var grid = parseGrid(input);

        var queue = new PriorityQueue<>(Comparator.comparingInt(Path::length));

        var farthest = 0L;
        queue.add(new Path(grid.start(), 0));
        var visited = new HashSet<Point>();
        while (!queue.isEmpty()) {
            var current = queue.poll();
            if (visited.contains(current.start())) {
                continue;
            }

            if (farthest < current.length()) {
                farthest = current.length();
            }

            visited.add(current.start());
            for (var neighbour : grid.graph().get(current.start())) {
                if (!visited.contains(neighbour)) {
                    queue.add(new Path(neighbour, current.length() + 1));
                }
            }
        }

        return farthest;
    }

    @Override
    public Long solvePart2(String input) {
        return solvePart1(input);
    }

    public static void main(String[] args) {
        new Day10().execute(10);
    }

    private Grid parseGrid(String input) {
        Point start = null;
        var graph = new HashMap<Point, List<Point>>();
        var lines = input.lines().toList();
        for (int y = 0; y < lines.size(); y++) {
            var lineLength = lines.get(y).length();
            for (var x = 0; x < lineLength; x++) {
                var point = new Point(x, y);
                List<Point> connectedWith = switch (lines.get(y).charAt(x)) {
                    case '|' -> List.of(point.up(), point.down());
                    case '-' -> List.of(point.left(), point.right());
                    case 'L' -> List.of(point.up(), point.right());
                    case 'J' -> List.of(point.up(), point.left());
                    case '7' -> List.of(point.down(), point.left());
                    case 'F' -> List.of(point.down(), point.right());
                    case 'S' -> {
                        start = Point.of(x, y);
                        yield new ArrayList<>(start.neighbours());
                    }
                    case '.' -> List.of();
                    default -> throw new IllegalStateException("Unexpected value: " + lines.get(y).charAt(x));
                };
                connectedWith = connectedWith.stream()
                        .filter(p -> p.x() >= 0
                                && p.y() >= 0
                                && p.x() < lines.size()
                                && p.y() < lineLength)
                        .toList();

                if (!connectedWith.isEmpty()) {
                    graph.put(point, connectedWith);
                }
            }
        }

        if (start == null) {
            throw new IllegalStateException("No start found.");
        }

        // Remove all nodes that are not connected to any other node.
        var y = graph.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream()
                        .filter(graph::containsKey)
                        .filter(p -> graph.get(p).contains(e.getKey()))
                        .toList()));

        return new Grid(start, y);
    }
}
