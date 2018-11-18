package com.dekinci.bot.game.tactics

import com.dekinci.bot.entities.StatedRiver
import com.dekinci.bot.game.map.GameMap
import org.junit.jupiter.api.Test

internal class ConnectMinesTacticsTest {
    fun createMap(): GameMap {
        val size = 9
        val minesList = listOf(3, 5)
        val riverList = "0,1 1,2 0,3 0,4 1,4 4,2 2,5 3,6 6,4 7,4 4,8 8,5 6,7 7,8"
                .split(" ")
                .map { StatedRiver(it.split(",")[0].toInt(), it.split(",")[1].toInt()) }

        return GameMap(size, riverList, minesList)
    }

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
        val tactics = ConnectMinesTactics(createMap(), 3, 5)
//        println(PathFinder(createMap()).findPath(3, 5))

        while (tactics.hasNext())
            println(tactics.next())
    }
}