package com.github.gjong.advent2023.days

import spock.lang.Specification

class Day3Test extends Specification {
    def "SolvePart1"() {
        given:
            def day = new Day3()
            def assignment = day.getClass().getResource("/day3.part1.txt").text
        when:
            def result = day.solvePart1(assignment)
        then:
            result == 4361
    }

    def "SolvePart2"() {
        given:
            def day = new Day3()
            def assignment = day.getClass().getResource("/day3.part1.txt").text
        when:
            def result = day.solvePart2(assignment)
        then:
            result == 467835
    }
}
