package com.dekinci.bot.game

import com.dekinci.bot.game.strategy.BuildEmpireStrategy
import com.dekinci.bot.moves.Move

class Intellect(private val gameState: GameState) {
    private var strategy = BuildEmpireStrategy(gameState)

    fun chooseMove(): Move {
        val move = strategy.next()
        println("Next move is $move")

        return move
    }
}