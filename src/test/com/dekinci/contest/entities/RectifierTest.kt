package com.dekinci.contest.entities

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class RectifierTest {
    private var r = Rectifier(setOf(River(0, 1)), setOf(0))

    @BeforeEach
    fun setUp() {
        r = Rectifier(listOf(
                River(1, 2),
                River(2, 3),
                River(2, 7)
        ), listOf(1, 3))
    }

    @Test
    fun asMap() {
        val expected = BasicMap(
                arrayOf(River(1, 0), River(2, 1), River(3, 1)),
                setOf(0, 2), 4)
        assertEquals(expected, r.asMap())
    }

    @Test
    fun purify() {
        assertEquals(0, r.purify(1))
        assertEquals(River(3, 1), r.purify(River(2, 7)))
    }

    @Test
    fun pollute() {
        assertEquals(1, r.pollute(0))
        assertEquals(River(2, 7), r.pollute(River(3, 1)))
    }
}