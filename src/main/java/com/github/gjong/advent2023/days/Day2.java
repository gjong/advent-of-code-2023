package com.github.gjong.advent2023.days;

/**
 * You're launched high into the atmosphere!
 * The apex of your trajectory just barely reaches the surface of a large island floating in the sky.
 * You gently land in a fluffy pile of leaves. It's quite cold, but you don't see much snow.
 * An Elf runs over to greet you.
 *
 * The Elf explains that you've arrived at Snow Island and apologizes for the lack of snow.
 * He'll be happy to explain the situation, but it's a bit of a walk, so you have some time.
 * They don't get many visitors up here; would you like to play a game in the meantime?
 *
 * As you walk, the Elf shows you a small bag and some cubes which are either red, green, or blue.
 * Each time you play this game, he will hide a secret number of cubes of each color in the bag, and your goal is to figure out information about the number of cubes.
 *
 * To get information, once a bag has been loaded with cubes, the Elf will reach into the bag,
 * grab a handful of random cubes, show them to you, and then put them back in the bag.
 * He'll do this a few times per game.
 *
 * You play several games and record the information from each game (your puzzle input).
 * Each game is listed with its ID number (like the 11 in Game 11: ...) followed by a semicolon-separated list of subsets of cubes that were revealed from the bag (like 3 red, 5 green, 4 blue).
 */
public class Day2 extends Executor<Integer> {

    record Game(int number, int green, int red, int blue) {
    }

    @Override
    public Integer solvePart1(String input) {
        // The Elf would first like to know which games would have been possible if the bag
        // contained only 12 red cubes, 13 green cubes, and 14 blue cubes?
        return input.lines()
                .map(this::createGame)
                .filter(game -> game.green() <= 13 && game.red() <= 12 && game.blue() <= 14)
                .mapToInt(Game::number)
                .sum();
    }

    /**
     * As you continue your walk, the Elf poses a second question: in each game you played,
     * what is the fewest number of cubes of each color that could have been in the bag to
     * make the game possible?
     */
    @Override
    public Integer solvePart2(String input) {
        return input.lines()
                .map(this::createGame)
                // The power of a set of cubes is equal to the numbers of red, green, and blue cubes
                // multiplied together.
                .mapToInt(game -> game.red() * game.green() * game.blue())
                .sum();
    }

    private Game createGame(String line) {
        String[] parts = line.split(":");

        var red = 0;
        var green = 0;
        var blue = 0;
        var number = Integer.parseInt(parts[0].substring(5));
        for (var segment : parts[1].split(";")) {
            for (var color : segment.split(",")) {
                var colorParts = color.trim().split(" ");
                var count = Integer.parseInt(colorParts[0]);
                var colorName = colorParts[1];
                switch (colorName) {
                    case "green" -> green = Math.max(green, count);
                    case "red" -> red = Math.max(red, count);
                    case "blue" -> blue = Math.max(blue, count);
                }
            }
        }

        return new Game(number, green, red, blue);
    }

    public static void main(String[] args) {
        new Day2().execute(2);
    }
}
