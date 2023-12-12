package com.github.gjong.advent2023.days

import spock.lang.Specification

class Day11Test extends Specification {
    def "SolvePart1"() {
        given:
            def day = new Day11()
            def assignment = day.getClass().getResource("/day11.part1.txt").text
        when:
            def result = day.solvePart1(assignment)
        then:
            result == 374
    }

    def "SolvePart2 - factor 10"() {
        given:
            def day = new Day11()
            def assignment = day.getClass().getResource("/day11.part1.txt").text
        when:
            def result = day.computeWithFactor(assignment, 10)
        then:
            result == 1030
    }

    def "SolvePart2 - factor 100"() {
        given:
            def day = new Day11()
            def assignment = day.getClass().getResource("/day11.part1.txt").text
        when:
            def result = day.computeWithFactor(assignment, 100)
        then:
            result == 8410
    }

}
