package com.dekinci.bot.game.map

import com.dekinci.bot.entities.River
import com.dekinci.bot.entities.RiverStateID
import com.dekinci.bot.game.GameState
import com.dekinci.bot.game.map.graphstuff.AdjacencyList
import com.dekinci.bot.game.map.graphstuff.AdjacencyMatrix
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class GameMap(size: Int, rivers: List<River>, minesCollection: Collection<Int>) {
    data class Island(val cost: Int, val sites: Set<Int>, val mines: Set<Int>)

    private val adjMatrix = AdjacencyMatrix(size, rivers)
    private val adjList = AdjacencyList(size, rivers)

    val sites: Set<Int>
    val mines = minesCollection.toHashSet()

    val islands: Set<Island>

    val ourSites = HashSet<Int>()

    val realMetrics = RealMetrics(size, adjList, mines)

    init {
        realMetrics.calculate()

        val siteSet = HashSet<Int>()
        rivers.forEach {
            siteSet.add(it.source)
            siteSet.add(it.target)
        }
        sites = siteSet

        islands = initIslands()
    }

    private fun initIslands(): Set<Island> {
        data class Island(var cost: Int, val sites: HashSet<Int>)

        val islandMap = HashMap<List<Int>, Island>()

        for (site in sites) {
            val determiner = realMetrics.getAllMines(site)

            if (determiner !in islandMap)
                islandMap[determiner] = Island(0, HashSet())

            val isl = islandMap[determiner]!!
            isl.cost += realMetrics.getForAllMines(site, mines)
            isl.sites.add(site)
        }

        return islandMap.values.map { GameMap.Island(it.cost, it.sites, it.sites.intersect(mines)) }.toSet()
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

    fun getFreeConnections(site: Int): Collection<Int> {
        return getConnections(site).filter { adjMatrix[site, it] == RiverStateID.NEUTRAL }
    }

    fun getAvailableConnections(site: Int): Collection<Int> {
        return getConnections(site).filter { adjMatrix[site, it] == RiverStateID.NEUTRAL || adjMatrix[site, it] == GameState.ID }
    }

    fun isSiteMine(site: Int): Boolean = mines.contains(site)

    fun claim(from: Int, to: Int, id: Int) {
        adjMatrix[from, to] = id

        if (id == GameState.ID) {
            ourSites.add(from)
            ourSites.add(to)
        }
    }
}