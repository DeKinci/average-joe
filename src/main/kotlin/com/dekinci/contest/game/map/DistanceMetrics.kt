package com.dekinci.contest.game.map

import com.dekinci.contest.entities.River
import com.dekinci.contest.game.map.graph.AdjacencyList
import com.dekinci.contest.game.map.graph.Dijkstra
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.util.concurrent.ConcurrentHashMap


class DistanceMetrics(
        private val sitesAmount: Int,
        private val totalList: AdjacencyList,
        private val mines: Set<Int>,
        private val operation: (Int) -> Int
) {
    private val weights = ConcurrentHashMap<Int, IntArray>(mines.size)

    fun calculate() {
        println("metrics started")
        val timestamp = System.currentTimeMillis()
        runBlocking {
            mines.map { async { calculateMineRelatedMetrics(it) } }
                    .forEach { it.await() }
        }

        println("metrics created for: ${(System.currentTimeMillis() - timestamp).toDouble() / 1000}")
    }

    private fun calculateMineRelatedMetrics(mine: Int) {
        weights[mine] = IntArray(sitesAmount) { -1 }

        val dijkstra = Dijkstra(sitesAmount, totalList)
        val metricsRelatedToMine = dijkstra.sparse(mine)

        metricsRelatedToMine.forEachIndexed { site, weight ->
            if (weight != -1)
                weights[mine]!![site] = operation.invoke(weight)
        }
    }

    fun mineCost(mine: Int) = weights[mine]!!.sumBy { if (it >= 0) it else 0 }

    fun siteCost(site: Int) = mines.sumBy {
        val weight = weights[it]!![site]
        if (weight >= 0) weight else 0
    }

    fun minSiteCost(site: Int) = mines.minBy {
        val weight = weights[it]!![site]
        if (weight >= 0) weight else Int.MAX_VALUE
    }

    fun maxSiteCost(site: Int) = mines.maxBy {
        val weight = weights[it]!![site]
        if (weight >= 0) weight else Int.MIN_VALUE
    }

    fun getJointMines(site: Int): Set<Int> = mines.filter { weights[it]!![site] > -1 }.toHashSet()

    fun costHavingMines(site: Int, mines: Collection<Int>): Int =
            mines.filter { weights[it]!![site] > -1 }.sumBy { weights[it]!![site] }

    fun costHavingSites(mine: Int, sites: Collection<Int>): Int =
            sites.sumBy { if (weights[mine]!![it] > -1) weights[mine]!![it] else 0 }

    fun costHaving(sites: Set<Int>): Int {
        val havingMines = sites.intersect(mines)
        return havingMines.sumBy { costHavingSites(it, sites) }
    }

    fun costHavingRivers(rivers: Set<River>): Int {
        val ownedMines = HashSet<Int>()
        rivers.forEach {
            if (mines.contains(it.target))
                ownedMines.add(it.target)
            if (mines.contains(it.source))
                ownedMines.add(it.source)
        }

        val cons = Cons(ownedMines, rivers)
        var result = 0

        runBlocking {
            result = cons.getGroups().map { group ->
                group.getMines().map {
                    async { costHavingSites(it, group.getSites()) }
                }
            }.flatten().map { it.await() }.sum()
        }

        return result
    }

    operator fun get(mine: Int, site: Int) = weights[mine]!![site]
}