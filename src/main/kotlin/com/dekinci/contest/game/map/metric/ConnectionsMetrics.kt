package com.dekinci.contest.game.map.metric

import com.dekinci.contest.game.map.graph.AdjacencyList

class ConnectionsMetrics(
        sitesAmount: Int,
        private val freeAdjList: AdjacencyList
) {
    private val connections = IntArray(sitesAmount) { freeAdjList.countConnections(it) }

    fun update(site: Int) {
        connections[site] = freeAdjList.countConnections(site)
    }

    operator fun get(site: Int) = connections[site]
}