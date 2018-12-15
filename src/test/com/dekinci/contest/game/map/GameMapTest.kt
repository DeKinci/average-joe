package com.dekinci.contest.game.map

import com.dekinci.contest.connectedMap
import com.dekinci.contest.disconnectedMap
import com.dekinci.contest.entities.StatedRiver
import com.dekinci.contest.microMap
import com.dekinci.contest.nanoMap
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class GameMapTest {
    //TODO

    @Test
    fun getIslands() {
        assertEquals(2, disconnectedMap().islands.size)
        assertEquals(1, connectedMap().islands.size)
        assertEquals(1, nanoMap().islands.size)
    }

    @Test
    fun update() {
        val gm = nanoMap()
        gm.update(StatedRiver(0, 1, 0))
        assertFalse(gm.hasFreeConnections(0))
        assertFalse(gm.hasFreeConnections(1))
    }

    @Test
    fun hasFreeConnections() {
        val gm = microMap()
        gm.update(StatedRiver(0, 1, 0))
        assertTrue(gm.hasFreeConnections(1))
        assertTrue(gm.hasFreeConnections(2))
        assertTrue(gm.hasFreeConnections(3))
        assertFalse(gm.hasFreeConnections(0))
    }

    @Test
    fun isSiteConnectedWithAny() {
        val gm = microMap()
        assertFalse(gm.isSiteConnectedWithAny(0, setOf(2, 3)))
        assertTrue(gm.isSiteConnectedWithAny(0, setOf(1)))
        assertTrue(gm.isSiteConnectedWithAny(0, setOf(1, 2, 3)))
    }

    @Test
    fun isSiteConnectedWith() {
        val gm = microMap()
        assertFalse(gm.isSiteConnectedWith(0, 2))
        assertTrue(gm.isSiteConnectedWith(0, 1))
    }

    @Test
    fun getConnections() {
        val gm = microMap()
        gm.update(StatedRiver(0, 1, 0))
        assertEquals(setOf(1), gm.getConnections(0))
        assertEquals(setOf(0, 2, 3), gm.getConnections(1))
    }

    @Test
    fun isSiteConnectedToTaken() {
        val gm = microMap()
        gm.update(StatedRiver(0, 1, 0))
        assertTrue(gm.isSiteConnectedToTaken(0))
        assertFalse(gm.isSiteConnectedToTaken(2))
    }

    @Test
    fun getFreeConnections() {
        val gm = microMap()
        gm.update(StatedRiver(0, 1, 0))
        assertEquals(emptyList<Int>(), gm.getFreeConnections(0))
        assertEquals(listOf(2, 3), gm.getFreeConnections(1))
        assertEquals(listOf(1), gm.getFreeConnections(2))
    }

    @Test
    fun getAvailableConnections() {
        val gm = microMap()
        gm.update(StatedRiver(0, 1, 0))
        gm.update(StatedRiver(2, 1, 1))

        assertEquals(listOf(1), gm.getAvailableConnections(0, 0))
        assertEquals(emptyList<Int>(), gm.getAvailableConnections(0, 1))
        assertEquals(listOf(0, 3), gm.getAvailableConnections(1, 0))
        assertEquals(listOf(2, 3), gm.getAvailableConnections(1, 1))
        assertEquals(emptyList<Int>(), gm.getAvailableConnections(2, 0))
        assertEquals(listOf(1), gm.getAvailableConnections(2, 1))
    }
}