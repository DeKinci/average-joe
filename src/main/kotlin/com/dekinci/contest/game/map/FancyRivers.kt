package com.dekinci.contest.game.map

import com.dekinci.contest.entities.River

class FancyRivers(private val gameMap: GameMap) {
    private val rivers = HashSet<River>()

    init {
        gameMap.basicMap.mines.forEach { siteChange(it) }
    }

    fun claim(from: Int, to: Int) {
        siteChange(from)
        siteChange(to)
    }

    private fun siteChange(site: Int) {
        val connections = gameMap.getFreeConnections(site)
        if (connections.isEmpty())
            rivers.removeAll { it.has(site) }
        else
            rivers.addAll(connections.map { River(site, it) })
    }

    fun getRivers(): Set<River> = rivers
}