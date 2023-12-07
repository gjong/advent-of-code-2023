package com.github.gjong.advent2023.days

import spock.lang.Specification

class Day7Test extends Specification {
    def "SolvePart1"() {
        given:
            def day = new Day7()
            def assignment = day.getClass().getResource("/day7.part1.txt").text
        when:
            def result = day.solvePart1(assignment)
        then:
            result == 6440
    }

    def "SolvePart2"() {
        given:
            def day = new Day7()
            def assignment = day.getClass().getResource("/day7.part1.txt").text
        when:
            def result = day.solvePart2(assignment)
        then:
            result == 71503
    }
}
