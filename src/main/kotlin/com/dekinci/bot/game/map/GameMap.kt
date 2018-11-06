package com.dekinci.bot.game.map

import com.dekinci.bot.entities.River
import com.dekinci.bot.entities.RiverStateID
import com.dekinci.bot.game.map.graphstuff.AdjacencyList
import com.dekinci.bot.game.map.graphstuff.AdjacencyMatrix
import java.util.*
import kotlin.collections.HashSet

class GameMap(size: Int, rivers: List<River>, mines: List<Int>) {
    private val adjMatrix = AdjacencyMatrix(size, rivers)
    private val adjList = AdjacencyList(size, rivers)
    private val minesSet = mines.toHashSet()

    val realMetrics = RealMetrics(size, adjList, mines)

    init {
        realMetrics.calculate()
    }

    fun hasFreeConnections(site: Int): Boolean = adjMatrix.hasFreeConnections(site)

    fun isSiteConnectedWithAny(first: Int, others: Collection<Int>) = !Collections.disjoint(adjList[first], others)

    fun isSiteConnectedWith(first: Int, second: Int) = adjList.list[first].contains(second)

    fun getConnections(site: Int) = adjList[site]

    fun getFreeConnections(site: Int): Collection<Int> {
        return getConnections(site).filter { adjMatrix[site, it] == RiverStateID.NEUTRAL }
    }

    fun isSiteMine(site: Int): Boolean = minesSet.contains(site)

    fun claim(from: Int, to: Int, id: Int) {
        adjMatrix[from, to] = id
    }
}