package com.dekinci.bot.game

import com.dekinci.bot.entities.RiverState
import com.dekinci.bot.utility.IDManager
import ru.spbstu.competition.protocol.data.Claim
import ru.spbstu.competition.protocol.data.Setup

class State(setup: Setup) {
    var rivers = RiverMap(IDManager.maxID, setup.map.rivers)
    var mines = ArrayList<Int>(50)
    var myId = -1

    init {
        myId = setup.punter

        for(mine in setup.map.mines)
            mines.add(mine)
    }

    fun update(claim: Claim) {
        rivers[claim.source, claim.target] = when(claim.punter) {
            myId -> RiverState.Our
            else -> RiverState.Enemy
        }
    }
}