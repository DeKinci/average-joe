package com.dekinci.bot.game

import com.dekinci.bot.game.map.GameMap
import com.dekinci.bot.utility.IDManager
import ru.spbstu.competition.protocol.data.Claim
import ru.spbstu.competition.protocol.data.Setup
import java.util.*

class GameState(private val setup: Setup) {
    val gameMap = GameMap(IDManager.maxID + 1, setup.map.rivers, setup.map.mines)

    companion object {
        var myId = -1
    }

    var mines = ArrayList<Int>(50)

    init {
        myId = setup.punter
        setupMines()
    }

    private fun setupMines() = setup.map.mines.forEach { mine ->
        if (gameMap.hasFreeConnections(mine)) {
            mines.add(mine)
        }
    }

    fun update(claim: Claim) = claimRiver(claim)

    private fun claimRiver(claim: Claim) {
        gameMap.claim(claim.source, claim.target, claim.punter)
    }
}