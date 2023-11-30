package com.github.gjong.advent2023.days;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public abstract class Executor<T> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Executes the solution for the given day.
     *
     * @param day The day to execute the solution for.
     */
    public void execute(int day) {
        var input = readInputData("/day" + day + "/input.txt");

        logger.info("Day {} - Part 1: {}", day, solvePart1(input));
        logger.info("Day {} - Part 2: {}", day, solvePart2(input));
    }

    /**
     * Solves part 1 of the given day.
     *
     * @param input The input data.
     * @return The solution for part 1.
     */
    public abstract T solvePart1(String input);

    /**
     * Solves part 2 of the given day.
     *
     * @param input The input data.
     * @return The solution for part 2.
     */
    public abstract T solvePart2(String input);

    /**
     * Reads the input data from the given file and returns it as a string.
     *
     * @param file The file to read from.
     * @return The input data as a string.
     */
    protected String readInputData(String file) {
        try (var inputData = readInputDataAsStream(file)) {
            return new String(inputData.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("Could not open input file.", e);
        }
    }

    private InputStream readInputDataAsStream(String file) {
        var inputStream = Executor.class.getResourceAsStream(file);
        if (inputStream == null) {
            throw new RuntimeException("Could not open input file.");
        }
        return inputStream;
    }
}
