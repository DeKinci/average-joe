package com.dekinci.bot.game.tactics

import com.dekinci.bot.game.GameState
import com.dekinci.bot.moves.Move

class GreedyTactics(private val gameState: GameState) : Tactics {
    private var isFinished = false
    private var isSuccessful = false

    override fun isFinished() = isFinished

    override fun isSuccessful() = isSuccessful

    override fun next(): Move {
        throw NoSuchElementException()
    }

    override fun hasNext(): Boolean {
        return false
    }
}