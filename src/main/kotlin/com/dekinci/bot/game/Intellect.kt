package com.dekinci.bot.game

import com.dekinci.bot.moves.Move
import com.dekinci.bot.moves.PassMove

class Intellect(private val state: State) {
    fun chooseMove(): Move {
        return PassMove()
    }
}