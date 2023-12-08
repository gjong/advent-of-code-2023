package com.github.gjong.advent2023.days;

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

    private long solve(String input, String start, String end) {
        var scanner = new Scanner(input);

        var moves = scanner.nextLine().toCharArray();
        var possibleMoves = processMoves(scanner);

        return possibleMoves.keySet()
                .stream()
                .filter(m -> m.endsWith(start))
                .mapToLong(pos -> {
                    var steps = 0;
                    while (true) {
                        if (pos.endsWith(end)) {
                            return steps;
                        }

                        pos = moves[steps++ % moves.length] == 'L'
                                ? possibleMoves.get(pos).left()
                                : possibleMoves.get(pos).right();
                    }
                })
                .reduce((left, right) -> {
                    var gcd = Math.abs(left);
                    var temp = Math.abs(right);
                    while (temp != 0) {
                        var t = temp;
                        temp = gcd % temp;
                        gcd = t;
                    }
                    return Math.abs(left * right) / gcd;
                })
                .orElseThrow();
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
