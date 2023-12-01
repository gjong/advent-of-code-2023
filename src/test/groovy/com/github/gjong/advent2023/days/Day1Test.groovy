package com.github.gjong.advent2023.days

import spock.lang.Specification

class Day1Test extends Specification {
    def "Solve Part 1"() {
        given:
            def day1 = new Day1()
            def assignment = day1.getClass().getResource("/day1.part1.txt").text
        when:
            def result = day1.solvePart1(assignment)
        then:
            result == 142
    }

    def "Solve Part 2"() {
        given:
            def day1 = new Day1()
            def assignment = day1.getClass().getResource("/day1.part2.txt").text
        when:
            def result = day1.solvePart2(assignment)
        then:
            result == 281
    }
}
