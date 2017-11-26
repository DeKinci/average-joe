package com.dekinci.bot.game.player

import com.dekinci.bot.game.map.GameMap
import java.util.ArrayList


class Player(
        private val map: GameMap,
        val sites: ArrayList<Int> = ArrayList(),
        val mines: ArrayList<Int> = ArrayList()) {
    var score = 0

    private val connectedSites = ArrayList<Int>()

    fun claimMine(mine: Int) {
        mines.add(mine)
        connectedSites.add(mine)
    }

    fun claimSite(site: Int) {
        if (map.isSiteConnectedWithAny(site, connectedSites))
            connectedSites.add(site)

        sites.add(site)
    }

    fun mineCost(mine: Int): Int = map.weightsRegistry.getForAllSites(mine, connectedSites)

    fun siteCost(site: Int): Int = if (site in connectedSites) map.weightsRegistry.getForAllMines(site, mines) else 0

    fun recount() {
        score = mines.sumBy { mineCost(it) }
    }
}