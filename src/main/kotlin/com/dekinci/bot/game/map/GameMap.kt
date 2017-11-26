package com.dekinci.bot.game.map

import com.dekinci.bot.entities.River
import com.dekinci.bot.game.map.graphstuff.AdjacencyList
import com.dekinci.bot.game.map.graphstuff.AdjacencyMatrix
import java.util.*

class GameMap(size: Int, rivers: List<River>, mines: List<Int>) {
    val riverAdjMatrix = AdjacencyMatrix(size, rivers)
    val totalList = AdjacencyList(size, rivers)
    val weightsRegistry = MetricsRegistry(size, totalList, mines)

    fun hasFreeConnections(site: Int): Boolean = riverAdjMatrix.hasFreeConnections(site)

    fun isSiteConnectedWithAny(first: Int, others: List<Int>) = !Collections.disjoint(totalList.list[first], others)

    fun isSiteConnectedWith(first: Int, second: Int) = totalList.list[first].contains(second)
}