package com.github.gjong.advent2023.days

import spock.lang.Specification

class Day2Test extends Specification {
    def "SolvePart1"() {
        given:
            def day = new Day2()
            def assignment = day.getClass().getResource("/day2.part1.txt").text
        when:
            def result = day.solvePart1(assignment)
        then:
            result == 8
    }

    def "SolvePart2"() {
        given:
            def day = new Day2()
            def assignment = day.getClass().getResource("/day2.part1.txt").text
        when:
            def result = day.solvePart2(assignment)
        then:
            result == 2286
    }
}
