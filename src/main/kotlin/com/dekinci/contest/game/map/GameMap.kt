package com.dekinci.contest.game.map

import com.dekinci.contest.entities.BasicMap
import com.dekinci.contest.entities.RiverStateID
import com.dekinci.contest.entities.StatedRiver
import com.dekinci.contest.game.map.graph.AdjacencyList
import com.dekinci.contest.game.map.graph.AdjacencyMatrix
import com.dekinci.contest.game.map.metric.ConnectionsMetrics
import com.dekinci.contest.game.map.metric.DistanceMetrics
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class GameMap(val basicMap: BasicMap) {
    data class Island(val cost: Int, val sites: Set<Int>, val mines: Set<Int>)

    private val adjMatrix = AdjacencyMatrix(basicMap.size, basicMap.rivers)
    private val adjList = AdjacencyList(basicMap.size, basicMap.rivers)

    private val freeAdjList = AdjacencyList(basicMap.size, basicMap.rivers)

    val islands: Set<Island>

    val squareMetrics = DistanceMetrics(basicMap.size, adjList, basicMap.mines) { it * it }
    val linearMetrics = DistanceMetrics(basicMap.size, adjList, basicMap.mines) { it }
    val connectionsMetrics = ConnectionsMetrics(basicMap.size, adjList)

    init {
        squareMetrics.calculate()
        linearMetrics.calculate()
        islands = initIslands()
    }

    private fun initIslands(): Set<Island> {
        data class TempIsland(var cost: Int, val sites: HashSet<Int> = HashSet())

        val islandMap = HashMap<Set<Int>, TempIsland>()

        (0 until basicMap.size).forEach {
            val connectedMines = squareMetrics.getJointMines(it)

            islandMap.getOrPut(connectedMines) { TempIsland(0) }.apply {
                cost += squareMetrics.costHavingMines(it, connectedMines)
                sites.add(it)
            }
        }

        return islandMap.entries.map { Island(it.value.cost, it.value.sites, it.key) }.toHashSet()
    }

    fun update(statedRiver: StatedRiver) {
        adjMatrix[statedRiver.source, statedRiver.target] = statedRiver.state
        freeAdjList.removeEdge(statedRiver.source, statedRiver.target)
        connectionsMetrics.update(statedRiver.source)
        connectionsMetrics.update(statedRiver.target)
    }

    fun hasFreeConnections(site: Int): Boolean = freeAdjList.countConnections(site) != 0

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

    fun getFreeConnections(site: Int): Collection<Int> = freeAdjList[site]

    fun getAvailableConnections(site: Int, punter: Int): Collection<Int> =
            getConnections(site).filter { adjMatrix[site, it] == RiverStateID.NEUTRAL || adjMatrix[site, it] == punter }
}