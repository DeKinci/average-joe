package com.dekinci.contest.game

import com.dekinci.contest.disconnectedMap
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