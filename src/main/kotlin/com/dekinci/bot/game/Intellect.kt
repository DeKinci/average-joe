package com.dekinci.bot.game

import com.dekinci.bot.entities.StatedRiver
import com.dekinci.bot.game.minimax.Minimax
import com.dekinci.bot.moves.ClaimMove
import com.dekinci.bot.moves.Move
import com.dekinci.bot.moves.PassMove
import ru.spbstu.competition.protocol.data.Claim
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.concurrent.atomic.AtomicReference

class Intellect(private val gameState: GameState) {
    private val maxRef = AtomicReference<Minimax>(Minimax(gameState.playersAmount, gameState.gameMap, 50))

    private val maxRunner = Executors.newSingleThreadExecutor { runnable ->
        Executors.defaultThreadFactory().newThread(runnable).also { it.isDaemon = true }
    }

    private var task: Future<*>? = null

    fun chooseMove(): Move {
        val move = chooseBest()

        println("Move is: $move")
        return move
    }

    private fun chooseBest(): Move {
        val river = maxRef.get().getBest(GameState.ID)

        if (river == null) {
            System.err.println("PassMove returned!")
            return PassMove()
        }

        gameState.update(Claim(GameState.ID, river.source, river.target))
        update(river.stated(GameState.ID))

        return ClaimMove(river.source, river.target)
    }

    fun update(river: StatedRiver) {
        maxRef.get().interrupt()
        task = maxRunner.submit {
            maxRef.get().update(river)
            maxRef.get().runCycle(GameState.ID)
        }
    }
}