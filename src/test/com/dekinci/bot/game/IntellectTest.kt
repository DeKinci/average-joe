package com.dekinci.bot.game

import com.dekinci.bot.disconnectedMap
import org.junit.jupiter.api.Test

internal class IntellectTest {

    fun createState(): GameState {
        return GameState(0, 1, disconnectedMap().basicMap)
    }

    @Test
    fun chooseMove() {
        val int = Intellect(createState())
        for (i in 0 .. 12)
            println(int.getRiver())
    }
}