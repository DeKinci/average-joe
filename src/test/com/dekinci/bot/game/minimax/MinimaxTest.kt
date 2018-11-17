package com.dekinci.bot.game.minimax

import com.dekinci.bot.entities.River
import com.dekinci.bot.game.map.GameMap
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class MinimaxTest {

    /**
     *  0---1---2
     *  | \ | /
     *  3   4   5
     *  | / |   |
     *  6---7   8
     */
    @Test
    fun test() {
        val size = 9
        val minesList = listOf(3, 2, 5)
        val riverList = "0,1 1,2 0,3 0,4 1,4 2,4 3,6 4,7 4,6 5,8 6,7"
                .split(" ")
                .map { River(it.split(",")[0].toInt(), it.split(",")[1].toInt()) }

        val map = GameMap(size, riverList, minesList)

        val pa = 4
        val depth = 1
        val mms = Array(pa) {Minimax(pa, map) }
        var i = 0
        var next = mms[i].findBest(depth, i)
        while (next != null) {
            println("$i: Taking ${next.source} ${next.target}")

            map.claim(next.source, next.target, next.state)
            mms.forEach { it.update(next!!.changeState(i)) }

            i++
            i %= pa
            next = mms[i].findBest(depth, i)
        }
    }
}