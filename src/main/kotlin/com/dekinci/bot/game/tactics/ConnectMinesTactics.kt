package com.dekinci.bot.game.tactics

import com.dekinci.bot.game.GameState
import com.dekinci.bot.moves.ClaimMove
import com.dekinci.bot.moves.Move
import com.dekinci.bot.moves.PassMove

class ConnectMinesTactics(
        gameState: GameState,
        private val from: Int,
        private val to: Int
) : Tactics {
    private val aStar = AStar(gameState.gameMap)
    private var steps = aStar.findPath(to, from)
    private var prevStep = 0

    override fun isFinished(): Boolean = prevStep == steps.size

    override fun isSuccessful(): Boolean = !steps.isEmpty()

    override fun hasNext(): Boolean = prevStep + 1 == (steps.size)

    override fun next(): Move {
        val newSteps = aStar.findPath(to, from)

        if (steps != newSteps) {
            prevStep = steps.commonUntil(newSteps)
            steps = newSteps
        }

        if (!hasNext())
            return PassMove()

        return ClaimMove(steps[prevStep], steps[++prevStep])
    }

    private fun <E> List<E>.commonUntil(other: List<E>): Int {
        val limit = if (size > other.size) other.size else size
        return (0 until limit).firstOrNull { this[it] != other[it] } ?: limit
    }
}
