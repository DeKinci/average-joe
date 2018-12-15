package com.dekinci.contest.game

import com.dekinci.contest.entities.StatedRiver
import com.dekinci.contest.game.minimax.Minimax
import java.util.concurrent.Executors
import java.util.concurrent.Future

class Intellect(private val gameState: GameState) {
    private val maxRef = Minimax(gameState.playersAmount, gameState.gameMap, gameState.punter, 50)

    private val maxRunner = Executors.newSingleThreadExecutor { runnable ->
        Executors.defaultThreadFactory().newThread(runnable).also { it.isDaemon = true }
    }

    private var task: Future<*>? = null

    fun getRiver(): StatedRiver? {
        if(true) return null
        val move = chooseBest()

        println("Move is: $move")
        return move
    }

    private fun chooseBest(): StatedRiver? {
        val river = maxRef.getBest(gameState.punter)

        if (river == null) {
            System.err.println("PassMove returned!")
            return null
        }

        gameState.update(river)
        update(river.stated(gameState.punter))

        return river
    }

    fun update(river: StatedRiver) {
        task = maxRunner.submit {
            maxRef.update(river)
            maxRef.startDoomMachine()
        }
    }
}