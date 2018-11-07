package com.dekinci.bot.game

import com.dekinci.bot.entities.CommonSite
import com.dekinci.bot.entities.River
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import ru.spbstu.competition.protocol.data.Map
import ru.spbstu.competition.protocol.data.Setup

internal class IntellectTest {

    fun createState(): GameState {
        val minesList = listOf(3, 5)
        val riverString = "0,1 1,2 0,3 0,4 1,4 4,2 2,5 3,6 6,4 7,4 4,8 8,5 6,7 7,8"

        val siteList = riverString.split(Regex("[ ,]")).map { it.toInt() }.toSet().toList().map { CommonSite(it) }

        val riverList = riverString.split(" ")
                .map { River(it.split(",")[0].toInt(), it.split(",")[1].toInt()) }

        return GameState(Setup(1, 1, Map(siteList, riverList, minesList), null))
    }

    @Test
    fun chooseMove() {
        val int = Intellect(createState())
        for (i in 0 .. 10)
            println(int.chooseMove())
    }
}