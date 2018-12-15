package com.dekinci.contest.game.map

import com.dekinci.contest.disconnectedMap
import com.dekinci.contest.entities.River
import com.dekinci.contest.nanoMap
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class ConsTest {

    @Test
    fun getGroupsNano() {
        val gm = nanoMap()
        val c = Cons(gm.basicMap.mines, gm.basicMap.rivers.asList())
        assertEquals(1, c.getGroups().size)
        assertEquals(1, c.getGroups().size)
        assertNotNull(c.getGroups().find { it.getSites() == setOf(1, 0) })
    }

    @Test
    fun getGroupsDisco() {
        val c = Cons(hashSetOf(2, 3), setOf(
                River(3, 6),
                River(7, 6),
                River(2, 1),
                River(0, 1)
        ))

        assertEquals(2, c.getGroups().size)
        assertNotNull(c.getGroups().find { it.getSites() == setOf(3, 6, 7) })
        assertNotNull(c.getGroups().find { it.getSites() == setOf(2, 1, 0) })
    }
}