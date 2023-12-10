package com.github.gjong.advent2023.days

import spock.lang.Specification

class Day10Test extends Specification {
    def "SolvePart1 - Solution 1"() {
        given:
            def day = new Day10()
            def assignment = day.getClass().getResource("/day10.part1.txt").text
        when:
            def result = day.solvePart1(assignment)
        then:
            result == 4
    }

    def "SolvePart1 - Solution 2"() {
        given:
            def day = new Day10()
            def assignment = day.getClass().getResource("/day10.part2.txt").text
        when:
            def result = day.solvePart1(assignment)
        then:
            result == 8
    }


    def "SolvePart2"() {
        given:
            def day = new Day10()
            def assignment = day.getClass().getResource("/day10.part4.txt").text
        when:
            def result = day.solvePart2(assignment)
        then:
            result == 31
    }
}
