package com.dekinci.bot.game.player

import com.dekinci.bot.entities.Mine
import com.dekinci.bot.entities.Site

class PlayersSite(private val id: Int, var isConnected: Boolean = false): Site {
    override fun getID(): Int = id
}