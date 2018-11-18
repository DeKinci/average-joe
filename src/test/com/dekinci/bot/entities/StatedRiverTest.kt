package com.dekinci.bot.entities

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

internal class StatedRiverTest {

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