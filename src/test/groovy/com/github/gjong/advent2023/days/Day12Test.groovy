package com.github.gjong.advent2023.days

import spock.lang.Specification

class Day12Test extends Specification {
    def day = new Day12()

    def "SolvePart1 - with broken"() {
        given:
            def assignment = day.getClass().getResource("/day12.part1.txt").text
        when:
            def result = day.solvePart1(assignment)
        then:
            result == 21
    }

    def "SolvePart1 - all known"() {
        given:
           def assignment = day.getClass().getResource("/day12.part2.txt").text
        when:
            def result = day.solvePart1(assignment)
        then:
            result == 6
    }

    def "SolvePart2 - factor 10"() {
        given:
            def assignment = day.getClass().getResource("/day12.part1.txt").text
        when:
            def result = day.solvePart2(assignment)
        then:
            result == 525152
    }

}
