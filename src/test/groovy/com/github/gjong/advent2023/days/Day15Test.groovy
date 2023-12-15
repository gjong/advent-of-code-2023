package com.github.gjong.advent2023.days

import spock.lang.Specification

class Day15Test extends Specification {
    def day = new Day15()

    def "SolvePart1"() {
        given:
            def assignment = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"
        when:
            def result = day.solvePart1(assignment)
        then:
            result == 1320
    }

    def "SolvePart2"() {
        given:
            def assignment = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"
        when:
            def result = day.solvePart2(assignment)
        then:
            result == 145
    }

}
