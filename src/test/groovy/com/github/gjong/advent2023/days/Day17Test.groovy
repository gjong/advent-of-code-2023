package com.github.gjong.advent2023.days

import spock.lang.Specification

class Day17Test extends Specification {
    def day = new Day17()

    def "SolvePart1"() {
        given:
            def assignment = day.getClass().getResource("/day17.part1.txt").text
        when:
            def result = day.solvePart1(assignment)
        then:
            result == 102
    }

}
