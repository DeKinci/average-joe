package com.dekinci.bot.game.tactics

import com.dekinci.bot.game.GameState

/**
 * This tactics implements connecting two networks via the shortest of possible paths.
 * It uses A* algorithm for best path finding and updates it state after each move
 * (to check if enemy captured one of desired rivers)
 * If no path exists, returns -1 in the nextMove
 */
class ConnectMinesTactics(
        gameState: GameState,
        private val from: Int,
        private val to: Int
) : Tactics, Iterator<Int> {
    private val aStar = AStar(gameState.gameMap)
    private var steps = aStar.findPath(to, from)
    private var prevStep = 0

    override fun isFinished(): Boolean = prevStep == steps?.size

    override fun isSuccessful(): Boolean = steps != null

    override fun nextNMoves(n: Int): List<Int> {
        return steps?.subList(prevStep, n) ?: listOf(-1)
    }

    override fun hasNext(): Boolean = prevStep + 1 == (steps?.size ?: 0)

    override fun next(): Int {
        val newSteps = aStar.findPath(to, from)

        if (steps != newSteps) {
            prevStep = newSteps?.let { steps?.commonUntil(it) } ?: 0
            steps = newSteps
        }

        if (!hasNext())
            return -1

        return steps!![prevStep++]
    }

    private fun <E> List<E>.commonUntil(other: List<E>): Int {
        val limit = if (size > other.size) other.size else size

        return (0 until limit).firstOrNull { this[it] != other[it] }
                ?: limit
    }
}
