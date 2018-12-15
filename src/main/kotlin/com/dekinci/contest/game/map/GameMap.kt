package com.dekinci.contest.game.map

import com.dekinci.contest.entities.BasicMap
import com.dekinci.contest.entities.RiverStateID
import com.dekinci.contest.entities.StatedRiver
import com.dekinci.contest.game.map.graph.AdjacencyList
import com.dekinci.contest.game.map.graph.AdjacencyMatrix
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class GameMap(val basicMap: BasicMap) {
    data class Island(val cost: Int, val sites: Set<Int>, val mines: Set<Int>)

    private val adjMatrix = AdjacencyMatrix(basicMap.size, basicMap.rivers)
    private val adjList = AdjacencyList(basicMap.size, basicMap.rivers)

    val islands: Set<Island>

    val realMetrics = RealMetrics(basicMap.size, adjList, basicMap.mines)

    init {
        realMetrics.calculate()
        islands = initIslands()
    }

    private fun initIslands(): Set<Island> {
        data class TempIsland(var cost: Int, val sites: HashSet<Int> = HashSet())

        val islandMap = HashMap<Set<Int>, TempIsland>()

        (0 until basicMap.size).forEach {
            val connectedMines = realMetrics.getJointMines(it)

            islandMap.getOrPut(connectedMines) { TempIsland(0) }.apply {
                cost += realMetrics.costHavingMines(it, connectedMines)
                sites.add(it)
            }
        }

        return islandMap.entries.map { Island(it.value.cost, it.value.sites, it.key) }.toHashSet()
    }

    fun update(statedRiver: StatedRiver) {
        adjMatrix[statedRiver.source, statedRiver.target] = statedRiver.state
    }

    fun hasFreeConnections(site: Int): Boolean = adjMatrix.hasFreeConnections(site)

    fun isSiteConnectedWithAny(first: Int, others: Collection<Int>) = !Collections.disjoint(adjList[first], others)

    fun isSiteConnectedWith(first: Int, second: Int) = adjList.list[first].contains(second)

    fun getConnections(site: Int) = adjList[site]

    fun isSiteConnectedToTaken(site: Int): Boolean {
        val connections = getConnections(site)
        for (c in connections)
            if (adjMatrix[site, c] != RiverStateID.NEUTRAL)
                return true
        return false
    }

    fun getFreeConnections(site: Int): Collection<Int> =
            getConnections(site).filter { adjMatrix[site, it] == RiverStateID.NEUTRAL }

    fun getAvailableConnections(site: Int, punter: Int): Collection<Int> =
            getConnections(site).filter { adjMatrix[site, it] == RiverStateID.NEUTRAL || adjMatrix[site, it] == punter }
}