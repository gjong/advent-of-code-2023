package com.github.gjong.advent2023.days

import spock.lang.Specification

class Day16Test extends Specification {
    def day = new Day16()

    def "SolvePart1"() {
        given:
            def assignment = day.getClass().getResource("/day16.part1.txt").text
        when:
            def result = day.solvePart1(assignment)
        then:
            result == 46
    }

}
