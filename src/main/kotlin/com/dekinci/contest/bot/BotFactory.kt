package com.dekinci.contest.bot

import com.dekinci.contest.entities.BasicMap

interface BotFactory {
    fun getBotName(): String

    fun makeBot(punter: Int, punters: Int, map: BasicMap): Bot
}