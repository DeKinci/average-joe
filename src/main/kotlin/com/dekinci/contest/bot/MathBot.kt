package com.dekinci.contest.bot

import com.dekinci.contest.common.riversToSiteSet
import com.dekinci.contest.entities.BasicMap
import com.dekinci.contest.entities.StatedRiver
import com.dekinci.contest.game.GameState
import com.dekinci.contest.game.map.Cons
import com.dekinci.contest.game.map.FancyRivers

class MathBot(override val name: String, punter: Int, punters: Int, map: BasicMap) : Bot {
    private val gameState = GameState(punter, punters, map)
    private val gm = gameState.gameMap
    private val fr = FancyRivers(gm)
    private val cons = Cons(gm.basicMap.mines)

    override fun onUpdate(statedRiver: StatedRiver) {
        gameState.update(statedRiver)
        fr.remove(statedRiver.stateless())
    }

    override fun getMove(): StatedRiver? {
        val max = riversToSiteSet(fr.getRivers()).toMutableList().maxWith(getComp())
        val move = max?.let { site -> fr.getRivers().find { it.has(site) } }?.stated(gameState.punter)
        move?.let { niceMove(it) }
        println("Moving: $move")
        return move
    }

    private fun getComp(): Comparator<Int> {
        val c = UltimateComparator()
        if (cons.getGroups().size > gm.islands.size)
            return c.reversed()
        return c
    }

    private fun niceMove(statedRiver: StatedRiver) {
        gameState.update(statedRiver)
        fr.update(statedRiver.stateless())
        cons.addRiver(statedRiver.stateless())
    }

    override fun onFinish() {}

    private inner class UltimateComparator : Comparator<Int> {
        private val comps = listOf(
                Comparator.comparing<Int, Int> { gm.linearMetrics.siteCost(it) },
                Comparator.comparing<Int, Int> { gm.squareMetrics.minSiteCost(it) }.reversed(),
                Comparator.comparing<Int, Int> { gm.squareMetrics.siteCost(it) },
                Comparator.comparing<Int, Int> { gm.squareMetrics.maxSiteCost(it) },
                Comparator.comparing<Int, Int> { gm.connectionsMetrics[it] }.reversed()
        )

        override fun compare(o1: Int, o2: Int): Int {
            for (comp in comps) {
                val result = comp.compare(o1, o2)
                if (result != 0)
                    return result
            }
            return 0
        }
    }
}