package com.dekinci.bot.game.tactics

import com.dekinci.bot.game.map.GameMap
import com.dekinci.bot.moves.ClaimMove
import com.dekinci.bot.moves.Move
import com.dekinci.bot.moves.PassMove

class ConnectMinesTactics(
        private val punter: Int,
        gameMap: GameMap,
        private val from: Int,
        private val to: Int
) : Tactics {
    private val aStar = PathFinder(gameMap)
    private var sites = aStar.findPath(from, to, punter)
    private var prevStep = 0
    private var currentSite = from

    override fun isFinished(): Boolean = prevStep == sites.size

    override fun isSuccessful(): Boolean = !sites.isEmpty()

    override fun hasNext() = prevStep < (sites.size)

    override fun next(): Move {
        val newSites = aStar.findPath(from, to, punter)

        if (sites != newSites) {
            println("path changed from $sites")
            println("path changed to   $newSites")

            println("prevstep changed from $prevStep")
            prevStep = sites.commonUntil(newSites)
            println("prevstep changed to   $prevStep")
            sites = newSites
        }

        if (!hasNext())
            return PassMove()

        val nextSite = sites[prevStep++]
        val move = ClaimMove(currentSite, nextSite)
        currentSite = nextSite

        return move
    }

    private fun <E> List<E>.commonUntil(other: List<E>): Int {
        val limit = if (size > other.size) other.size else size
        return (0 until limit).firstOrNull { this[it] != other[it] } ?: limit
    }
}
