package com.dekinci.bot.game.minimax

import com.dekinci.bot.entities.River
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
     *  0-o-1-o-2
     *  x \ | /
     *  3   4   5
     *  | / o   |
     *  6-x-7   8
     */
    private fun disconnectedMap(): GameMap {
        val size = 9
        val minesList = listOf(3, 2, 5)
        val riverList = "0,1 1,2 0,3 0,4 1,4 2,4 3,6 4,7 4,6 5,8 6,7"
                .split(" ")
                .map { River(it.split(",")[0].toInt(), it.split(",")[1].toInt()) }

        return GameMap(size, riverList, minesList)
    }

    /**
     *  0---1
     */
    private fun nanoMap() = GameMap(2, listOf(River(0, 1)), listOf(0))

    @Test
    fun test() {
        val map = disconnectedMap()

        val pa = 2
        val depth = 1
        val mms = Array(pa) {Minimax(pa, map, depth) }
        var i = 0
        mms[i].runCycle(i)
        var next = mms[i].getBest(i)
        while (next != null) {
            println("$i: Taking ${next.source} ${next.target}")

            map.claim(next.source, next.target, next.state)
            mms.forEach { it.update(next!!.stated(i)) }

            i++
            i %= pa
            mms[i].runCycle(i)
            next = mms[i].getBest(i)
        }

        println(Stat)
    }

    @Test
    fun testSmall() {
        val map = nanoMap()

        val pa = 2
        val depth = 40
        val mms = Array(pa) {Minimax(pa, map, depth) }
        var i = 0
        mms[i].runCycle(i)
        var next = mms[i].getBest(i)
        while (next != null) {
            println("$i: Taking ${next.source} ${next.target}")

            map.claim(next.source, next.target, next.state)
            mms.forEach { it.update(next!!.stated(i)) }

            i++
            i %= pa
            mms[i].runCycle(i)
            next = mms[i].getBest(i)
        }

        println(Stat)
    }

    @Test
    fun siteChange() {
        val map = disconnectedMap()

        val mm = Minimax(1, map, 0)
        val rivers = hashSetOf<River>()

        map.mines.forEach{ mm.siteChange(rivers, it) }
        println(rivers)
        map.claim(0, 3, 1000)
        mm.riverChange(rivers, River(0, 3))
        println(rivers)
        map.claim(0, 4, 1000)
        mm.riverChange(rivers, River(0, 4))
        println(rivers)
    }
}