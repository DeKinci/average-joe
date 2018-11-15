package com.dekinci.bot.game

import com.dekinci.bot.moves.ClaimMove
import com.dekinci.bot.moves.Move

class Intellect(private val gameState: GameState) {

    fun chooseMove(): Move {
        val river = gameState.fancySites.getSites().random()
        gameState.fancySites.claim(river.source, river.target)
        return ClaimMove(river.source, river.target)
    }
}