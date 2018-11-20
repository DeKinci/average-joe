package com.dekinci.bot.game.minimax

import com.dekinci.bot.entities.River
import com.dekinci.bot.entities.StatedRiver
import com.dekinci.bot.game.map.GameMap
import org.junit.jupiter.api.Test

internal class MinimaxTest {

    /**
     *  0---1---2
     *  | \ | /
     *  3   4   5
     *  | / |   |
     *  6---7   8
     */

    /**
     *  0-x-1-x-2
     *  x x x x
     *  3   4   5
     *  | / x   x
     *  6-x-7   8
     */
    private fun disconnectedMap(): GameMap {
        val size = 9
        val minesList = listOf(3, 2, 5)
        val riverList = "0,1 1,2 0,3 0,4 1,4 2,4 3,6 4,7 4,6 5,8 6,7"
                .split(" ")
                .map { StatedRiver(it.split(",")[0].toInt(), it.split(",")[1].toInt()) }

        return GameMap(size, riverList, minesList)
    }

    @Test
    fun test() {
        val map = disconnectedMap()

        val pa = 1
        val depth = 10
        val mms = Array(pa) {Minimax(pa, map) }
        var i = 0
        var next = mms[i].findBest(depth, i)
        while (next != null) {
            println("$i: Taking ${next.source} ${next.target}")

            map.claim(next.source, next.target, next.state)
            mms.forEach { it.update(next!!.stated(i)) }

            i++
            i %= pa
            next = mms[i].findBest(depth, i)
        }
    }

    @Test
    fun siteChange() {
        val map = disconnectedMap()

        val mm = Minimax(2, map)
        val rivers = hashSetOf<River>()

        map.mines.forEach{ mm.siteChange(rivers, it) }
        println(rivers)
        mm.siteChange(rivers, 0)
        println(rivers)
        mm.siteChange(rivers, 3)
        println(rivers)
    }

}