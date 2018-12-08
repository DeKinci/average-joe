package com.dekinci.bot.game

import com.dekinci.bot.entities.StatedRiver
import com.dekinci.bot.game.minimax.Minimax
import com.dekinci.bot.moves.ClaimMove
import com.dekinci.bot.moves.Move
import com.dekinci.bot.moves.PassMove
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.concurrent.atomic.AtomicReference

class Intellect(private val gameState: GameState) {
    private val maxRef = AtomicReference<Minimax>(Minimax(gameState.playersAmount, gameState.gameMap))
    private val maxRunner = Executors.newSingleThreadExecutor { runnable ->
        Executors.defaultThreadFactory().newThread(runnable).also { it.isDaemon = true }
    }

    fun chooseMove(): Move {
        val move = chooseBest()

        println("Move is: $move")
        return move
    }

    private fun chooseBest(): Move {
        val task = maxRunner.submit {
            maxRef.get().runCycle(1000, GameState.ID)
        }

        try {
            task.get(300, TimeUnit.MILLISECONDS)
        } catch (e: TimeoutException) {
            maxRef.get().interrupt()
            task.get()
        }

        val river = maxRef.get().getBest(GameState.ID)
        if (river != null) {
            gameState.gameMap.claim(river.source, river.target, GameState.ID)
            maxRef.get().update(river.stated(GameState.ID))
        }
        return river?.let { ClaimMove(river.source, river.target) } ?: PassMove()
    }

    fun update(river: StatedRiver) {
        maxRunner.submit {
            maxRef.get().update(river)
        }.get()
    }
}