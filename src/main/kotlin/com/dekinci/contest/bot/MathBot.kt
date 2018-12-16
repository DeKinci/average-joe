package com.dekinci.contest.bot

import com.dekinci.contest.common.riversToSiteSet
import com.dekinci.contest.entities.BasicMap
import com.dekinci.contest.entities.StatedRiver
import com.dekinci.contest.game.GameState
import com.dekinci.contest.game.map.Cons
import com.dekinci.contest.game.map.FancyRivers

class MathBot(override val name: String, punter: Int, punters: Int, map: BasicMap) : Bot {
    private enum class Time {
        GATHER,
        SCATTER
    }

    private val gameState = GameState(punter, punters, map)
    private val gm = gameState.gameMap
    private val fr = FancyRivers(gm)
    private val cons = Cons(gm.basicMap.mines)

    private var time = Time.GATHER

    override fun onUpdate(statedRiver: StatedRiver) {
        if (statedRiver.state != gameState.punter) {
            gameState.update(statedRiver)
            fr.remove(statedRiver.stateless())
        }
    }

    override fun getMove(): StatedRiver? {
        val max = riversToSiteSet(fr.getRivers()).toMutableList().maxWith(getComp())
        if (max != null) {
            val maxes = fr.getRivers().filter { it.has(max) }
            val move = maxes.random().stated(gameState.punter)
            niceMove(move)
            return move
        }

        return null
    }

    private fun getComp(): Comparator<Int> {
        return if (time == Time.GATHER && cons.getGroups().size > gm.islands.size) {
            time = Time.SCATTER
            UltimateComparator()
        } else
            UltimateComparator().reversed()
    }

    private fun niceMove(statedRiver: StatedRiver) {
        gameState.update(statedRiver)
        fr.update(statedRiver.stateless())
        cons.addRiver(statedRiver.stateless())
    }

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