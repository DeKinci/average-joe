package com.dekinci.contest.game.tactics

import com.dekinci.contest.entities.StatedRiver
import com.dekinci.contest.game.map.GameMap

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

    override fun next(): StatedRiver? {
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
            return null

        val nextSite = sites[prevStep++]
        val river = StatedRiver(currentSite, nextSite, punter)
        currentSite = nextSite

        return river
    }

    private fun <E> List<E>.commonUntil(other: List<E>): Int {
        val limit = if (size > other.size) other.size else size
        return (0 until limit).firstOrNull { this[it] != other[it] } ?: limit
    }
}
