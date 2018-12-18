package com.dekinci.contest.bot

import com.dekinci.contest.entities.BasicMap
import com.dekinci.contest.entities.River
import com.dekinci.contest.entities.StatedRiver
import com.dekinci.contest.game.GameState
import com.dekinci.contest.game.map.Cons
import com.dekinci.contest.game.map.FancyRivers
import kotlin.math.roundToInt

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
        val c = getComp()
        val max = fr.getRivers().maxWith(c)
        if (max != null) {
            val maxes = fr.getRivers().filter { c.compare(it, max) == 0 }
            val move = maxes.random().stated(gameState.punter)
            niceMove(move)
            return move
        }

        return null
    }

    private fun getComp(): Comparator<River> {
        return if (time == Time.GATHER && cons.getGroups().size > gm.islands.size) {
            time = Time.SCATTER
            ScatterComparator()
        } else
            GatherComparator()
    }

    private fun niceMove(statedRiver: StatedRiver) {
        gameState.update(statedRiver)
        fr.update(statedRiver.stateless())
        cons.addRiver(statedRiver.stateless())
    }

    private inner class GatherComparator : Comparator<River> {
        private val comps = listOf(
                Comparator.comparing<River, Int> {
                    val delta = Math.abs(gm.squareMetrics.siteCost(it.source) - gm.squareMetrics.siteCost(it.target))
                    if (delta == 0)
                        Int.MAX_VALUE
                    else
                        delta
                }.reversed(),
                Comparator.comparing<River, Int> {
                    Math.min(gm.connectionsMetrics[it.source], gm.connectionsMetrics[it.target])
                }.reversed()
        )

        override fun compare(o1: River, o2: River): Int {
            for (comp in comps) {
                val result = comp.compare(o1, o2)
                if (result != 0)
                    return result
            }
            return 0
        }
    }

    private inner class ScatterComparator : Comparator<River> {
        private val comps = listOf(
                Comparator.comparing<River, Int> {
                    Math.abs(gm.squareMetrics.siteCost(it.source) - gm.squareMetrics.siteCost(it.target))
                },
                Comparator.comparing<River, Int> {
                    Math.min(gm.connectionsMetrics[it.source], gm.connectionsMetrics[it.target])
                }.reversed()
        )

        override fun compare(o1: River, o2: River): Int {
            for (comp in comps) {
                val result = comp.compare(o1, o2)
                if (result != 0)
                    return result
            }
            return 0
        }
    }
}