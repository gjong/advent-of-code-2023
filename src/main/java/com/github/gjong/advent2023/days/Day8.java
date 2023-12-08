package com.github.gjong.advent2023.days;

import com.github.gjong.advent2023.common.algo.Algo;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * It seems like you're meant to use the left/right instructions to navigate the network.
 * Perhaps if you have the camel follow the same instructions, you can escape the haunted wasteland!
 * <p>
 * After examining the maps for a bit, two nodes stick out: AAA and ZZZ.
 * You feel like AAA is where you are now, and you have to follow the left/right instructions until you reach ZZZ.
 */
public class Day8 extends Executor<Long> {

    /**
     * This record represents a possible move in the network.
     * It contains the current position and the positions to the left and right.
     */
    record PossibleMove(String position, String left, String right) {
    }

    @Override
    public Long solvePart1(String input) {
        return solve(input, "AAA", "ZZZ");
    }


    /**
     * After examining the maps a bit longer, your attention is drawn to a curious fact:
     * the number of nodes with names ending in A is equal to the number ending in Z! If you were a ghost,
     * you'd probably just start at every node that ends with A and follow all of the paths at the same time
     * until they all simultaneously end up at nodes that end with Z.
     */
    @Override
    public Long solvePart2(String input) {
        return solve(input, "A", "Z");
    }

    public static void main(String[] args) {
        new Day8().execute(8);
    }

    /**
     * This method solves the problem.
     * It processes the input to get the possible moves and then navigates the network.
     */
    private long solve(String input, String start, String end) {
        var scanner = new Scanner(input);

        var moves = scanner.nextLine().toCharArray();
        var possibleMoves = processMoves(scanner);

        return possibleMoves.keySet()
                .stream()
                .filter(m -> m.endsWith(start))
                .mapToLong(pos -> solve(possibleMoves, moves, pos, end))
                .reduce(Algo::lcm)
                .orElseThrow();
    }

    /**
     * This method navigates the network from the start node to the end node.
     * It uses the directions array to decide whether to move left or right at each step.
     */
    private long solve(Map<String, PossibleMove> moves, char[] directions, String start, String end) {
        var steps = 0;
        while (!start.endsWith(end)) {
            start = directions[steps++ % directions.length] == 'L'
                    ? moves.get(start).left()
                    : moves.get(start).right();
        }
        return steps;
    }

    /**
     * Format of the input is:
     * AAA = (BBB, CCC)
     * BBB = (DDD, EEE)
     * CCC = (ZZZ, GGG)
     */
    private Map<String, PossibleMove> processMoves(Scanner input) {
        Pattern pattern = Pattern.compile("(\\w+)\\s=\\s\\((\\w+),\\s(\\w+)\\)");

        Map<String, PossibleMove> moves = new HashMap<>();
        while (input.hasNextLine()) {
            Matcher matcher = pattern.matcher(input.nextLine());
            if (matcher.find()) {
                String position = matcher.group(1);
                String leftMove = matcher.group(2);
                String rightMove = matcher.group(3);

                moves.put(position, new PossibleMove(position, leftMove, rightMove));
            }

        }
        return moves;
    }
}
