package com.dekinci.contest.bot

import com.dekinci.contest.entities.BasicMap
import com.dekinci.contest.entities.StatedRiver
import com.dekinci.contest.game.GameState
import com.dekinci.contest.game.Intellect

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