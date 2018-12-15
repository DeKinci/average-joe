package com.dekinci.bot

import com.dekinci.bot.entities.BasicMap

interface BotFactory {
    fun getBotName(): String

    fun makeBot(punter: Int, punters: Int, map: BasicMap): Bot
}