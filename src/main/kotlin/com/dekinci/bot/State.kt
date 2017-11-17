package com.dekinci.bot

import ru.spbstu.competition.protocol.data.Claim
import ru.spbstu.competition.protocol.data.Setup

class State(setup: Setup) {
    val rivers = RiverMap(setup.map.sites, setup.map.rivers)
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