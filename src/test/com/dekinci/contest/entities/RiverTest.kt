package com.dekinci.contest.entities

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.lang.IllegalStateException

internal class RiverTest {

    @Test
    fun has() {
        val r = River(5, 10)
        assertTrue(r.has(5))
        assertTrue(r.has(10))
        assertFalse(r.has(7))
    }

    @Test
    fun another() {
        val r = River(5, 10)
        assertEquals(10, r.another(5))
        assertEquals(5, r.another(10))
        assertThrows(IllegalStateException::class.java) { r.another(7) }
    }

    @Test
    fun equalsTest() {
        val r1 = River(5, 10)
        val r2 = River(10, 5)

        assertEquals(r1, r2)
        assertEquals(r2, r1)
    }

    @Test
    fun hashCodeTest() {
        val r1 = River(5, 10)
        val r2 = River(10, 5)

        assertEquals(r1.hashCode(), r2.hashCode())
    }

    @Test
    fun stated() {
        val sr = StatedRiver(5, 10, 0)
        val r = River(10, 5)

        assertEquals(sr, r.stated(0))
    }
}