package com.dekinci.bot.game

import com.dekinci.bot.entities.River
import com.dekinci.bot.entities.RiverState
import com.dekinci.bot.game.map.GameMap
import com.dekinci.bot.utility.IDManager
import ru.spbstu.competition.protocol.data.Claim
import ru.spbstu.competition.protocol.data.Setup
import java.util.*

class State(private val setup: Setup) {
    val riverMap = GameMap(IDManager.maxID + 1, setup.map.rivers, setup.map.mines)

    var ourRivers = ArrayList<River>()
    var enemyRivers = ArrayList<River>()

    var interestingSites = TreeMap<Int, SortedMap<Int, Int>>()

    var mines = ArrayList<Int>(50)
    var myId = -1

    init {
        myId = setup.punter
        setupMines()
    }

    private fun setupMines() = setup.map.mines.forEach { mine ->
        if (riverMap.hasConnections(mine)) {
            tryAddInterestingSite(mine)
            mines.add(mine)
        }
    }

    fun investigateInterestingSites() {

    }

    fun update(claim: Claim) = claimRiver(claim, when(claim.punter) {
        myId -> RiverState.Our
        else -> RiverState.Enemy
    })

    private fun claimRiver(claim: Claim, state: RiverState) {
        riverMap.riverAdjMatrix.riverMatrix[claim.source, claim.target] = state

        when (state) {
            RiverState.Our -> ourRivers
            else -> enemyRivers
        }.add(River(claim, state))

        tryAddInterestingSite(claim.source)
        tryAddInterestingSite(claim.target)
    }

    private fun tryAddInterestingSite(site: Int) {
        if (!interestingSites.contains(site) && riverMap.hasConnections(site))
            riverMap.weightsRegistry.weights[site]?.let { interestingSites.put(site, it) }
    }
}