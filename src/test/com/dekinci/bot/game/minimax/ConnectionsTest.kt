package com.dekinci.bot.game.minimax

import com.dekinci.bot.entities.River
import com.dekinci.bot.game.map.GameMap
import com.dekinci.bot.game.map.RealMetrics
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

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

        val newC = Connections(gm.mines)
                .addRiver(River(3, 0))
                .addRiver(River(0, 1))
                .addRiver(River(3, 6))
                .addRiver(River(4, 7))
                .addRiver(River(5, 8))

        assertEquals(7, newC.cost(gm.realMetrics))
    }

    /**
     *  0---1---2
     *  | \ | /
     *  3   4   5
     *  | / |   |
     *  6---7   8
     */
    private fun disconnectedMap(): GameMap {
        val size = 9
        val minesList = listOf(3, 2, 5)
        val riverList = "0,1 1,2 0,3 0,4 1,4 2,4 3,6 4,7 4,6 5,8 6,7"
                .split(" ")
                .map { River(it.split(",")[0].toInt(), it.split(",")[1].toInt()) }

        return GameMap(size, riverList, minesList)
    }
}