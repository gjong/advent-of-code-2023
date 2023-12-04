package com.github.gjong.advent2023.days

import spock.lang.Specification

class Day4Test extends Specification {
    def "SolvePart1"() {
        given:
            def day = new Day4()
            def assignment = day.getClass().getResource("/day4.part1.txt").text
        when:
            def result = day.solvePart1(assignment)
        then:
            result == 13
    }

    def "SolvePart2"() {
        given:
            def day = new Day4()
            def assignment = day.getClass().getResource("/day4.part1.txt").text
        when:
            def result = day.solvePart2(assignment)
        then:
            result == 30
    }
}
