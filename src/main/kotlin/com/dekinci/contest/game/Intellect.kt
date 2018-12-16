package com.dekinci.contest.game

import com.dekinci.contest.common.Log.debug
import com.dekinci.contest.common.Log.warn
import com.dekinci.contest.entities.StatedRiver
import com.dekinci.contest.game.minimax.Minimax
import java.util.concurrent.Executors

class Intellect(private val gameState: GameState) {
    private val maxRef = Minimax(gameState.playersAmount, gameState.gameMap, gameState.punter, Int.MAX_VALUE)

    private val maxRunner = Executors.newSingleThreadExecutor { runnable ->
        Executors.defaultThreadFactory().newThread(runnable).also { it.isDaemon = true }
    }

    init {
        maxRunner.submit {
            maxRef.mineSolution()
        }
    }

    fun getRiver(): StatedRiver? {
        val move = chooseBest()

        debug("Move is: $move")
        return move
    }

    private fun chooseBest(): StatedRiver? {
        Thread.sleep(400)
        val river = maxRef.getBest(gameState.punter)

        if (river == null) {
            warn("PassMove returned!")
            return null
        }

        gameState.update(river)
        update(river.stated(gameState.punter))

        return river
    }

    fun update(river: StatedRiver) {
        maxRef.interrupt()
        maxRunner.submit {
            maxRef.update(river)
            maxRef.mineSolution()
        }
    }

    fun finish() {
        maxRef.interrupt()
    }
}