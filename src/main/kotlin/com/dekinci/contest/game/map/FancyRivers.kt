package com.dekinci.contest.game.map

import com.dekinci.contest.entities.River

class FancyRivers(private val gameMap: GameMap) {
    private val rivers = HashSet<River>()

    init {
        gameMap.basicMap.mines.forEach { siteChange(it) }
    }

    fun update(river: River) {
        rivers.remove(river)
        siteChange(river.target)
        siteChange(river.source)
    }

    fun remove(river: River) {
        rivers.remove(river)
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