package com.github.gjong.advent2023.days

import spock.lang.Specification

class Day8Test extends Specification {
    def "SolvePart1"() {
        given:
            def day = new Day8()
            def assignment = day.getClass().getResource("/day8.part1.txt").text
        when:
            def result = day.solvePart1(assignment)
        then:
            result == 6
    }

    def "SolvePart2"() {
        given:
            def day = new Day8()
            def assignment = day.getClass().getResource("/day8.part2.txt").text
        when:
            def result = day.solvePart2(assignment)
        then:
            result == 6
    }
}
