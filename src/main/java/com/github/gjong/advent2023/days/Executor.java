package com.github.gjong.advent2023.days;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

public abstract class Executor<T> {

    protected final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    /**
     * Executes the solution for the given day.
     *
     * @param day The day to execute the solution for.
     */
    public Executor<T> execute(int day) {
        var input = readInputData("/day" + day + "/input.txt");

        execute(day, 1, () -> solvePart1(input));
        execute(day, 2, () -> solvePart2(input));
        return this;
    }

    private void execute(int day, int part, Supplier<T> runnable) {
        var start = Instant.now();
        var result = runnable.get();
        var end = Instant.now();

        var correct = assertIfSolution(day, part, result);
        logger.info(
                "Part {}: {} ({}ms). - {}",
                part,
                result,
                Duration.between(start, end).toMillis(),
                correct ? "[GOOD]" : "[BAD]");
    }

    private boolean assertIfSolution(int day, int part, T solution) {
        try (var resource = Executor.class.getResourceAsStream("/day" + day + "/part"+ part +".txt")) {
            if (resource == null) {
                return false;
            }

            var expected = Long.parseLong(new String(resource.readAllBytes()));
            return solution.equals(expected);
        } catch (IOException e) {
            throw new RuntimeException("Could not open solution file.", e);
        }
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
