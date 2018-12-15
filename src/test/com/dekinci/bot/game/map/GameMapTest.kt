package com.dekinci.bot.game.map

import com.dekinci.bot.connectedMap
import com.dekinci.bot.disconnectedMap
import com.dekinci.bot.entities.BasicMap
import com.dekinci.bot.entities.StatedRiver
import com.dekinci.bot.nanoMap
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class GameMapTest {

    @Test
    fun getIslands() {
        assertEquals(2, disconnectedMap().islands)
        assertEquals(1, connectedMap().islands)
        assertEquals(1, nanoMap().islands)
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
    }

    @Test
    fun isSiteConnectedWithAny() {
    }

    @Test
    fun isSiteConnectedWith() {
    }

    @Test
    fun getConnections() {
    }

    @Test
    fun isSiteConnectedToTaken() {
    }

    @Test
    fun getFreeConnections() {
    }

    @Test
    fun getAvailableConnections() {
    }

    @Test
    fun getBasicMap() {
    }
}