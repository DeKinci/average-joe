package com.dekinci.bot

import com.dekinci.bot.entities.BasicMap
import com.dekinci.bot.entities.StatedRiver
import com.dekinci.bot.game.GameState
import com.dekinci.bot.game.Intellect

class BotImpl(override val name: String, punter: Int, punters: Int, map: BasicMap) : Bot {
    private val gameState = GameState(punter, punters, map)
    private val intellect = Intellect(gameState)

    override fun onUpdate(statedRiver: StatedRiver) {
        gameState.update(statedRiver)
        intellect.update(statedRiver)
    }

    override fun getMove(): StatedRiver? {
        return intellect.getRiver()
    }
}