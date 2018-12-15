package com.dekinci.bot.game.player

import com.dekinci.bot.game.map.GameMap


class Player(
        private val map: GameMap,
        val sites: MutableSet<Int> = HashSet(),
        val mines: MutableSet<Int> = HashSet()) {
    var score = 0

    private fun claim(site: Int): Int {
        if (site in map.basicMap.mines)
            mines.add(site)

        if (sites.add(site))
            return recount()
        return 0
    }

    private fun recount(): Int {
        val oldScore = score
        score = map.realMetrics.getHaving(sites.union(mines))
        return score - oldScore
    }
}