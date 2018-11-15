package com.dekinci.bot.game.player

import com.dekinci.bot.game.map.GameMap


class Player(
        private val map: GameMap,
        val sites: MutableSet<Int> = HashSet(),
        val mines: MutableSet<Int> = HashSet()) {
    var score = 0

    private fun claim(site: Int): Int {
        if (site in map.mines)
            return claimMine(site)
        return claimSite(site)
    }

    private fun claimMine(mine: Int): Int {
        if (mine !in mines) {
            mines.add(mine)
            return recount()
        }
        return 0
    }

    fun claimSite(site: Int): Int {
        if (site !in sites) {
            sites.add(site)
            return recount()
        }
        return 0
    }

    private fun recount(): Int {
        val oldScore = score
        score = mines.sumBy { map.realMetrics.getForAllSites(it, sites.union(mines)) }
        return score - oldScore
    }
}