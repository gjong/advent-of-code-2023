package com.github.gjong.advent2023.days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * The almanac (your puzzle input) lists all of the seeds that need to be planted.
 * It also lists what type of soil to use with each kind of seed, what type of fertilizer
 * to use with each kind of soil, what type of water to use with each kind of fertilizer,
 * and so on. Every type of seed, soil, fertilizer and so on is identified with a number,
 * but numbers are reused by each category - that is, soil 123 and fertilizer 123 aren't
 * necessarily related to each other.
 */
public class Day5 extends Executor<Long> {

    private record Seed(long seed, long additional, Seed original) {
        private Seed {
            if (additional < 0) {
                throw new IllegalArgumentException("Additional must be positive");
            }
        }

        Seed before(long position) {
            return new Seed(
                    seed,
                    position - seed - 1,
                    this);
        }

        Seed bounded(long start, long end) {
            return new Seed(
                    Math.max(seed, start),
                    Math.min(additional, end - seed),
                    this);
        }

        Seed after(long position) {
            return new Seed(
                    position + 1,
                    additional - (position - seed) - 1,
                    this);
        }

        long last() {
            return seed + additional;
        }

        @Override
        public String toString() {
            return "Seed{ " + seed + ", " + (seed + additional) + " }";
        }
    }

    private record Mutation(long start, long destination, long size) {
        boolean within(Seed seed) {
            return (start <= seed.seed())
                    && (start + size >= seed.seed() + seed.additional());
        }

        boolean overlap(Seed seed) {
            var endAfterStart = seed.seed() + seed.additional() >= start;
            var startBeforeEnd = seed.seed() <= start + size;

            return endAfterStart && startBeforeEnd;
        }

        Seed mutate(Seed seed) {
            var location = destination + seed.seed() - start;
            return new Seed(location, seed.additional(), seed);
        }

        long last() {
            return start + size;
        }

        @Override
        public String toString() {
            return "Mutation{ "
                    + start + "-" + (start + size)
                    + " -> " + destination + "-" + (destination + size)
                    + " }";
        }
    }

    @Override
    public Long solvePart1(String input) {
        var sections = input.split("\\n\\n");

        var seeds = parseSeeds(sections[0]);
        for (var mutationSet = 1; mutationSet < sections.length; mutationSet++) {
            var mutations = parseMutations(sections[mutationSet]);

            seeds = seeds.stream()
                    .flatMap(s -> mutate(s, mutations).stream())
                    .toList();
        }

        return seeds.stream()
                .mapToLong(Seed::seed)
                .min()
                .getAsLong();
    }

    /**
     * Everyone will starve if you only plant such a small number of seeds.
     * Re-reading the almanac, it looks like the seeds: line actually describes ranges
     * of seed numbers.
     */
    @Override
    public Long solvePart2(String input) {
        var sections = input.split("\\n\\n");

        var seeds = parseUpdated(sections[0]);
        for (var mutationSet = 1; mutationSet < sections.length; mutationSet++) {
            var mutations = parseMutations(sections[mutationSet]);

            seeds = mutate(seeds, mutations);
        }

        return seeds.stream()
                .mapToLong(Seed::seed)
                .min()
                .getAsLong();
    }

    public static void main(String[] args) {
        new Day5().execute(5);
    }

    private List<Seed> mutate(List<Seed> seeds, List<Mutation> mutations) {
        return seeds.stream()
                .flatMap(s -> mutate(s, mutations).stream())
                .toList();
    }

    private List<Seed> mutate(Seed seed, List<Mutation> mutations) {
        logger.trace("Processing seed {}.", seed);

        for (var mutation : mutations) {
            if (mutation.within(seed)) {
                logger.debug("Seed {} within with mutation {}", seed, mutation);
                return List.of(mutation.mutate(seed));
            } else if (mutation.overlap(seed)) {
                logger.debug("Seed {} overlaps with mutation {}", seed, mutation);

                List<Seed> splitSeeds = new ArrayList<>();
                if (seed.seed() < mutation.start()) {
                    splitSeeds.add(seed.before(mutation.start()));
                }
                splitSeeds.add(seed.bounded(mutation.start(), mutation.last()));
                if (seed.last() > mutation.last()) {
                    splitSeeds.add(seed.after(mutation.last()));
                }

                logger.debug("Split seeds: {}", splitSeeds);
                return mutate(splitSeeds, mutations);
            }
        }

        return List.of(seed);
    }

    private List<Mutation> parseMutations(String input) {
        var mutationInstruction = input.split("\\n");

        var mappingPattern = Pattern.compile("(\\d+)\\s(\\d+)\\s(\\d+)");

        return IntStream.range(1, mutationInstruction.length)
                .mapToObj(i -> mappingPattern.matcher(mutationInstruction[i]))
                .filter(Matcher::matches)
                .map(matcher -> new Mutation(
                        Long.parseLong(matcher.group(2)),
                        Long.parseLong(matcher.group(1)),
                        Long.parseLong(matcher.group(3)) - 1))
                .toList();
    }

    private List<Seed> parseUpdated(String input) {
        var pattern = Pattern.compile("(\\d+)\\s(\\d+)", Pattern.DOTALL);
        var matcher = pattern.matcher(input);

        var seeds = new ArrayList<Seed>();
        while (matcher.find()) {
            var start = Long.parseLong(matcher.group(1));
            var amount = Long.parseLong(matcher.group(2)) - 1;
            seeds.add(new Seed(start, amount, null));
        }

        return seeds;
    }

    private List<Seed> parseSeeds(String input) {
        return Arrays.stream(input.substring(7).split("\\s"))
                .map(Long::parseLong)
                .map(n -> new Seed(n, 0, null))
                .toList();
    }

}
