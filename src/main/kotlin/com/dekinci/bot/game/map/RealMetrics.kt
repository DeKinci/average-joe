package com.dekinci.bot.game.map

import com.dekinci.bot.game.map.graphstuff.AdjacencyList
import com.dekinci.bot.game.map.graphstuff.Dijkstra
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors

class RealMetrics(
        private val sitesAmount: Int,
        private val totalList: AdjacencyList,
        private val mines: List<Int>
) {
    private val weights = ConcurrentHashMap<Int, IntArray>(mines.size)

    fun calculate() {
        println("metrics started")
        val timestamp = System.currentTimeMillis()
        val executor = Executors.newCachedThreadPool()
        mines.map { executor.submit { calculateMineRelatedMetrics(it) } }.forEach { it.get() }

        executor.shutdown()
        println("metrics created for: ${(System.currentTimeMillis() - timestamp).toDouble() / 1000}")
    }

    private fun calculateMineRelatedMetrics(mine: Int) {
        weights[mine] = IntArray(sitesAmount) { -1 }

        val dijkstra = Dijkstra(sitesAmount, totalList)
        val metricsRelatedToMine = dijkstra.sparse(mine).map { w -> w * w }

        metricsRelatedToMine.forEachIndexed { site, weight ->
            if (weight != -1)
                weights[mine]!![site] = weight
        }
    }

    fun getForAllMines(site: Int, mines: Collection<Int>): Int = mines.sumBy { weights[it]?.get(site) ?: 0 }

    fun getForAllSites(mine: Int, sites: Collection<Int>): Int = sites.sumBy { weights[mine]?.get(it) ?: 0 }

    operator fun get(mine: Int, site: Int) = weights[mine]?.get(site) ?: 0
}