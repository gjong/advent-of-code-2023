package com.github.gjong.advent2023.days

import spock.lang.Specification

class Day13Test extends Specification {
    def day = new Day13()

    def "SolvePart1"() {
        given:
            def assignment = day.getClass().getResource("/day13.part1.txt").text
        when:
            def result = day.solvePart1(assignment)
        then:
            result == 405
    }

    def "SolvePart2"() {
        given:
            def assignment = day.getClass().getResource("/day13.part1.txt").text
        when:
            def result = day.solvePart2(assignment)
        then:
            result == 400
    }

}
