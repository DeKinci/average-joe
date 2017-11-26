package com.dekinci.bot.game

import com.dekinci.bot.game.map.GameMap
import com.dekinci.bot.game.scoring.PlayersManager
import com.dekinci.bot.utility.IDManager
import ru.spbstu.competition.protocol.data.Claim
import ru.spbstu.competition.protocol.data.Setup
import java.util.*

class State(private val setup: Setup) {
    val gameMap = GameMap(IDManager.maxID + 1, setup.map.rivers, setup.map.mines)
    val manager = PlayersManager(gameMap, setup.punters)

    companion object {
        var myId = -1
    }

    var interestingSites = TreeMap<Int, SortedMap<Int, Int>>()

    var mines = ArrayList<Int>(50)

    init {
        myId = setup.punter
        setupMines()
    }

    private fun setupMines() = setup.map.mines.forEach { mine ->
        if (gameMap.hasFreeConnections(mine)) {
            tryAddInterestingSite(mine)
            mines.add(mine)
        }
    }

    fun update(claim: Claim) = claimRiver(claim, claim.punter)

    private fun claimRiver(claim: Claim, state: Int) {
        gameMap.riverAdjMatrix.matrix[claim.source, claim.target] = state

        tryAddInterestingSite(claim.source)
        tryAddInterestingSite(claim.target)
    }

    private fun tryAddInterestingSite(site: Int) {
        if (!interestingSites.contains(site) && gameMap.hasFreeConnections(site))
            gameMap.weightsRegistry.weights[site]?.let { interestingSites.put(site, it) }
    }
}