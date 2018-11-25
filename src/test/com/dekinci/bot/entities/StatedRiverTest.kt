package com.dekinci.bot.entities

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import java.lang.IllegalStateException

internal class StatedRiverTest {

    @Test
    fun has() {
        val r = StatedRiver(5, 10)
        Assertions.assertTrue(r.has(5))
        Assertions.assertTrue(r.has(10))
        Assertions.assertFalse(r.has(7))
    }

    @Test
    fun another() {
        val r = StatedRiver(5, 10)
        assertEquals(10, r.another(5))
        assertEquals(5, r.another(10))
        Assertions.assertThrows(IllegalStateException::class.java) { r.another(7) }
    }

    @Test
    fun equalsTest() {
        val r1 = StatedRiver(5, 10)
        val r2 = StatedRiver(10, 5)
        val r3 = StatedRiver(10, 5, 1)

        assertEquals(r1, r2)
        assertEquals(r2, r1)

        assertNotEquals(r1, r3)
        assertNotEquals(r2, r3)
    }

    @Test
    fun hashCodeTest() {
        val r1 = StatedRiver(5, 10)
        val r2 = StatedRiver(10, 5)
        val r3 = StatedRiver(10, 5, 1)

        assertEquals(r1.hashCode(), r2.hashCode())
        assertNotEquals(r1.hashCode(), r3.hashCode())
        assertNotEquals(r2.hashCode(), r3.hashCode())
    }
}