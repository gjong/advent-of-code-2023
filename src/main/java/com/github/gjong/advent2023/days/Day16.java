package com.github.gjong.advent2023.days;

import com.github.gjong.advent2023.common.CharGrid;
import com.github.gjong.advent2023.common.geometry.Point;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day16 extends Executor<Long> {

    private record Movement(Point position, Direction direction) {}

    public enum Direction {
        RIGHT(Point.zero.right()),
        DOWN(Point.zero.down()),
        LEFT(Point.zero.left()),
        UP(Point.zero.up());
        private final Point vector;

        Direction(Point vector) {
            this.vector = vector;
        }
    }

    private final Map<Character, Map<Direction, List<Direction>>> conversion = Map.of(
            '.', Map.of(Direction.RIGHT, List.of(Direction.RIGHT), Direction.DOWN, List.of(Direction.DOWN), Direction.LEFT, List.of(Direction.LEFT), Direction.UP, List.of(Direction.UP)),
            '|', Map.of(Direction.RIGHT, List.of(Direction.DOWN, Direction.UP),Direction.DOWN, List.of(Direction.DOWN),Direction.LEFT, List.of(Direction.DOWN, Direction.UP),Direction.UP, List.of(Direction.UP)),
            '-', Map.of(Direction.RIGHT, List.of(Direction.RIGHT),Direction.DOWN, List.of(Direction.RIGHT, Direction.LEFT),Direction.LEFT, List.of(Direction.LEFT),Direction.UP, List.of(Direction.RIGHT, Direction.LEFT)),
            '\\', Map.of(Direction.RIGHT, List.of(Direction.DOWN), Direction.DOWN, List.of(Direction.RIGHT),Direction.LEFT, List.of(Direction.UP),Direction.UP, List.of(Direction.LEFT)),
            '/', Map.of(Direction.RIGHT, List.of(Direction.UP),Direction.DOWN, List.of(Direction.LEFT),Direction.LEFT, List.of(Direction.DOWN),Direction.UP, List.of(Direction.RIGHT)));

    @Override
    public Long solvePart1(String input) {
        return solve(new Movement(new Point(0, 0), Direction.RIGHT), new CharGrid(input));
    }

    @Override
    public Long solvePart2(String input) {
        var grid = new CharGrid(input);
        // since the grid is square, we can start at all edges at once
        return IntStream.range(0, grid.rows())
                .mapToObj(r -> Stream.of(
                        new Movement(Point.of(r, 0), Direction.DOWN),
                        new Movement(Point.of(r, grid.cols() - 1), Direction.UP),
                        new Movement(Point.of(0, r), Direction.RIGHT),
                        new Movement(Point.of(grid.rows() - 1, r), Direction.LEFT))
                )
                .flatMap(Function.identity())
                .parallel()
                .mapToLong(start -> solve(start, grid))
                .max()
                .getAsLong();
    }

    public static void main(String[] args) {
        new Day16().execute(16);
    }

    private long solve(Movement start, CharGrid grid) {
        var bounds = grid.bounds();

        var queue = new Stack<Movement>();
        queue.add(start);

        var visited = new HashSet<Movement>();
        while (!queue.isEmpty()) {
            var move = queue.pop();
            for (var directions : getDirections(grid.at(move.position.x(), move.position.y()), move.direction)) {
                var nextPoint = move.position.translate(directions.vector);
                var nextState = new Movement(nextPoint, directions);
                if (bounds.inBounds(nextPoint) && !visited.contains(nextState)) {
                    visited.add(nextState);
                    queue.add(nextState);
                }
            }
        }

        return visited.stream()
                .map(m -> m.position)
                .distinct()
                .count() + 1;
    }

    private List<Direction> getDirections(char c, Direction direction) {
        return conversion.get(c).get(direction);
    }
}
