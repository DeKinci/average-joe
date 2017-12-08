package com.dekinci.bot.game.player

import com.dekinci.bot.game.map.GameMap
import java.util.ArrayList


class Player(
        private val map: GameMap,
        val sites: HashSet<Int> = HashSet(),
        val mines: HashSet<Int> = HashSet()) {
    var score = 0

    private val connectedSites = ArrayList<Int>()

    fun claimSite(site: Int) {
        if (sites.contains(site))
            return

        if (map.isSiteMine(site))
            claimMine(site)

        if (map.isSiteConnectedWithAny(site, connectedSites))
            connectedSites.add(site)

        sites.add(site)
    }

    private fun claimMine(mine: Int) {
        if (mines.contains(mine))
            return

        mines.add(mine)
        connectedSites.add(mine)
    }

    fun mineCost(mine: Int): Int = map.weightsRegistry.getForAllSites(mine, connectedSites)

    fun siteCost(site: Int): Int = if (site in connectedSites) map.weightsRegistry.getForAllMines(site, mines) else 0

    fun recount() {
        score = mines.sumBy { mineCost(it) }
    }
}