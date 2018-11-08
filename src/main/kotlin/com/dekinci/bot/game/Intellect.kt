package com.dekinci.bot.game

import com.dekinci.bot.moves.Move
import com.dekinci.bot.moves.PassMove

class Intellect(private val gameState: GameState) {

    fun chooseMove(): Move {
        return PassMove()
    }
}