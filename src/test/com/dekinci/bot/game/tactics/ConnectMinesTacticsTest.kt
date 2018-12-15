package com.dekinci.bot.game.tactics

import com.dekinci.bot.connectedMap
import com.dekinci.bot.entities.River
import com.dekinci.bot.entities.StatedRiver
import com.dekinci.bot.game.map.GameMap
import org.junit.jupiter.api.Test

internal class ConnectMinesTacticsTest {

    @Test
    fun isFinished() {
    }

    @Test
    fun isSuccessful() {
    }

    @Test
    fun hasNext() {
    }

    @Test
    operator fun next() {
        val tactics = ConnectMinesTactics(0, connectedMap(), 3, 5)

        while (tactics.hasNext())
            println(tactics.next())
    }
}