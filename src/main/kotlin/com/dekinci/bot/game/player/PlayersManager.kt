package com.dekinci.bot.game.player

import com.dekinci.bot.game.map.GameMap

class PlayersManager(private val map: GameMap, playersAmount: Int) {
    private val players = ArrayList<Player>(playersAmount)

    init {
        for (i in 0 until playersAmount)
            players.add(Player(map))
    }

    fun claimMine(mine: Int, punter: Int) {
        players[punter].claimMine(mine)
    }

    fun claimSite(site: Int, punter: Int) {
        players[punter].claimSite(site)
    }
}