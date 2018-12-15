package com.dekinci.contest.game.minimax

import com.dekinci.contest.disconnectedMap
import com.dekinci.contest.entities.River
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ConnectionsTest {
    private var connections = Connections(emptySet())

    @BeforeEach
    fun setCons() {
        connections = Connections(setOf(1, 2, 6))
    }

    @Test
    fun addRiver() {
        val newC = connections
                .addRiver(River(4, 1))
                .addRiver(River(7, 10))
                .addRiver(River(4, 6))
        println(newC)
        println(newC.addRiver(River(6, 7)))
    }

    @Test
    fun cost() {
        val gm = disconnectedMap()

        val newC = Connections(gm.basicMap.mines)
                .addRiver(River(3, 0))
                .addRiver(River(0, 1))
                .addRiver(River(3, 6))
                .addRiver(River(4, 7))
                .addRiver(River(5, 8))

        assertEquals(7, newC.cost(gm.realMetrics))
    }
}