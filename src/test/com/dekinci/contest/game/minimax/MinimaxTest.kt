package com.dekinci.contest.game.minimax

import com.dekinci.contest.disconnectedMap
import com.dekinci.contest.entities.River
import com.dekinci.contest.entities.StatedRiver
import com.dekinci.contest.nanoMap
import org.junit.jupiter.api.Test

internal class MinimaxTest {

    @Test
    fun test() {
        val map = disconnectedMap()

        val pa = 2
        val depth = 15
        val mms = Array(pa) { Minimax(pa, map, it, depth) }
        var i = 0
        mms[i].mineSolution()
        var next = mms[i].getBest(i)
        var counter = 0
        while (next != null) {
            println("$i: Taking ${next.source} ${next.target}")
            counter++

            map.update(StatedRiver(next.source, next.target, next.state))
            mms.forEach { it.update(next!!.stated(i)) }

            i++
            i %= pa
            mms[i].mineSolution()
            next = mms[i].getBest(i)
        }

        println(Stat)
        println(counter)
    }

    @Test
    fun testSmall() {
        val map = nanoMap()

        val pa = 2
        val depth = 40
        val mms = Array(pa) { Minimax(pa, map, it, depth) }
        var i = 0
        mms[i].mineSolution()
        var next = mms[i].getBest(i)
        while (next != null) {
            println("$i: Taking ${next.source} ${next.target}")

            map.update(StatedRiver(next.source, next.target, next.state))
            mms.forEach { it.update(next!!.stated(i)) }

            i++
            i %= pa
            mms[i].mineSolution()
            next = mms[i].getBest(i)
        }

        println(Stat)
    }

    @Test
    fun siteChange() {
//        val map = disconnectedMap()

//        val mm = Minimax(1, map, 1, 0)
        val rivers = hashSetOf<River>()

//        map.basicMap.mines.forEach{ mm.siteChange(rivers, it) }
//        println(rivers)
//        map.update(StatedRiver(0, 3, 1000))
//        mm.riverChange(rivers, River(0, 3))
//        println(rivers)
//        map.update(StatedRiver(0, 4, 1000))
//        mm.riverChange(rivers, River(0, 4))
        println(rivers)
    }

    @Test
    fun getFancyRivers() {
        val map = disconnectedMap()
        val mm = Minimax(1, map, 0, 0)

        println(mm.getFancyRivers(setOf(StatedRiver(3, 0, 0)), 0))
    }
}