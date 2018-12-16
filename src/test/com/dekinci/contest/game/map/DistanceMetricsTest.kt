package com.dekinci.contest.game.map

import com.dekinci.contest.disconnectedMap
import com.dekinci.contest.entities.River
import com.dekinci.contest.microMap
import com.dekinci.contest.nanoMap
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DistanceMetricsTest {

    @Test
    fun mineCost() {
        val nano = nanoMap().squareMetrics
        assertEquals(1, nano.mineCost(0))

        val micro = microMap().squareMetrics
        assertEquals(9, micro.mineCost(0))
        assertEquals(9, micro.mineCost(2))

        val disco = disconnectedMap().squareMetrics
        assertEquals(1, disco.mineCost(5))
        assertEquals(23, disco.mineCost(3))
        assertEquals(23, disco.mineCost(2))
    }

    @Test
    fun siteCost() {
        val micro = microMap().squareMetrics
        assertEquals(4, micro.siteCost(0))
        assertEquals(2, micro.siteCost(1))
    }

    @Test
    fun getJointMines() {
        val micro = microMap().squareMetrics
        assertEquals(setOf(0, 2), micro.getJointMines(1))

        val disco = disconnectedMap().squareMetrics
        assertEquals(setOf(2, 3), disco.getJointMines(4))
        assertEquals(setOf(2, 3), disco.getJointMines(2))
        assertEquals(setOf(5), disco.getJointMines(8))
    }

    @Test
    fun costHavingMines() {
        val disco = disconnectedMap().squareMetrics
        assertEquals(1, disco.costHavingMines(4, setOf(2)))
        assertEquals(0, disco.costHavingMines(2, setOf(2)))
        assertEquals(9, disco.costHavingMines(2, setOf(2, 3)))
        assertEquals(8, disco.costHavingMines(7, setOf(2, 3)))
        assertEquals(0, disco.costHavingMines(7, setOf(5)))
    }

    @Test
    fun costHavingSites() {
        val disco = disconnectedMap().squareMetrics
        assertEquals(1, disco.costHavingSites(2, setOf(4)))
        assertEquals(5, disco.costHavingSites(2, setOf(4, 7)))
        assertEquals(0, disco.costHavingSites(2, setOf(5, 8)))
        assertEquals(1, disco.costHavingSites(5, setOf(5, 8)))
        assertEquals(0, disco.costHavingSites(3, setOf(3)))
    }

    @Test
    fun costHaving() {
        val disco = disconnectedMap().squareMetrics
        assertEquals(1, disco.costHaving(setOf(2, 4)))
        assertEquals(33, disco.costHaving(setOf(0, 1, 2, 3, 4)))
        assertEquals(1, disco.costHaving(setOf(5, 8, 6, 0, 1)))
        assertEquals(0, disco.costHaving(setOf(7, 8, 0)))
        assertEquals(0, disco.costHaving(setOf(3)))
    }

    @Test
    fun costHavingRivers() {
        val disco = disconnectedMap().squareMetrics

        assertEquals(0, disco.costHavingRivers(setOf(River(1, 4))))
        assertEquals(1, disco.costHavingRivers(setOf(River(2, 4))))
        assertEquals(2, disco.costHavingRivers(setOf(River(2, 4), River(3, 0))))
        assertEquals(5, disco.costHavingRivers(setOf(River(2, 4), River(4, 0))))
        assertEquals(28, disco.costHavingRivers(setOf(
                River(2, 4),
                River(4, 0),
                River(3, 0))))

        assertEquals(29, disco.costHavingRivers(setOf(
                River(2, 4),
                River(4, 0),
                River(3, 0),
                River(5, 8))))
    }

    @Test
    fun get() {
        val disco = disconnectedMap().squareMetrics

        assertEquals(0, disco[2, 2])
        assertEquals(9, disco[2, 3])
        assertEquals(-1, disco[3, 8])
        assertEquals(4, disco[3, 4])
    }
}