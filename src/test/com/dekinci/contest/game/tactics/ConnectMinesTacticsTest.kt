package com.dekinci.contest.game.tactics

import com.dekinci.contest.connectedMap
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