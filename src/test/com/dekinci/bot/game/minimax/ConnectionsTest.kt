package com.dekinci.bot.game.minimax

import com.dekinci.bot.entities.River
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

    }
}