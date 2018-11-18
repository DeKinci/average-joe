package com.dekinci.bot.game

import com.dekinci.bot.entities.StatedRiver
import com.dekinci.bot.game.minimax.Minimax
import com.dekinci.bot.moves.ClaimMove
import com.dekinci.bot.moves.Move
import com.dekinci.bot.moves.PassMove

class Intellect(private val gameState: GameState) {
    private val max = Minimax(gameState.playersAmount, gameState.gameMap)

    fun chooseMove(): Move {
        val river = max.findBest(1, GameState.ID)
        if (river != null) {
            gameState.gameMap.claim(river.source, river.target, GameState.ID)
            max.update(river.stated(GameState.ID))
        }
        val move = river?.let { ClaimMove(river.source, river.target) } ?: PassMove()

        println("StatedRiver is: $river")
        return move
    }

    fun update(river: StatedRiver) {
        max.update(river)
    }
}