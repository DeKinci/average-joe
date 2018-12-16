package com.dekinci.contest.bot

import com.dekinci.contest.disconnectedMap
import com.dekinci.contest.entities.StatedRiver
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class MathBotTest {

    @Test
    fun test() {
        val map = disconnectedMap()
        val bot = MathBot("", 0, 1, map.basicMap)

        var next: StatedRiver? = bot.getMove()
        while (next != null) {
            bot.onUpdate(next)
            println(next)
            next = bot.getMove()
        }
    }

    @Test
    fun testCo() {
        val p = 2
        val map = disconnectedMap()
        val bots = Array<Bot>(p) { MathBot("", it, p, map.basicMap) }

        var i = 0
        var next: StatedRiver? = bots[i].getMove()
        while (next != null) {
            println(next)
            bots.forEachIndexed { ind, b -> if (ind != i) b.onUpdate(next!!)  }

            i = ++i % p
            next = bots[i].getMove()
        }
    }
}