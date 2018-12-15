package com.dekinci.contest.game.player

import com.dekinci.contest.game.GameState

class PlayersManager(gameState: GameState, playersAmount: Int) {
    private val players = ArrayList<Player>(playersAmount)

    init {
        for (i in 0 until playersAmount)
            players.add(Player(gameState.gameMap))
    }

    fun claimSite() {
        TODO()
    }
}