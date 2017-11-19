package com.dekinci.bot.game.map

import com.dekinci.bot.entities.River

class GameMap(size: Int, rivers: List<River>, mines: List<Int>) {
    val riverAdjMatrix = AdjacencyMatrix(size, rivers)
    val weightsRegistry = WeightsRegistry(riverAdjMatrix, mines)

    fun find(predicate: (River) -> Boolean): River? = riverAdjMatrix.find(predicate)

    fun findAll(predicate: (River) -> Boolean): ArrayList<River> = riverAdjMatrix.findAll(predicate)

    fun hasConnections(site: Int): Boolean = riverAdjMatrix.hasFreeConnections(site)
}