package com.dekinci.bot.game.player

import com.dekinci.bot.game.GameState

class PlayersManager(private val gameState: GameState, playersAmount: Int) {
    private val players = ArrayList<Player>(playersAmount)

    init {
        for (i in 0 until playersAmount)
            players.add(Player(gameState.gameMap))
    }

    fun claimSite(site: Int, punter: Int) {
//        players[punter].claimSite(site) TODO
    }
}