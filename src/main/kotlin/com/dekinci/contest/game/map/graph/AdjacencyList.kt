package com.dekinci.contest.game.map.graph

import com.dekinci.contest.entities.River

class AdjacencyList(vertexAmount: Int, rivers: Array<River>) {
    val list: Array<HashSet<Int>> = Array(vertexAmount) { HashSet<Int>() }

    init {
        rivers.forEach { river ->
            addEdge(river.source, river.target)
        }
    }

    fun addEdge(from: Int, to: Int) {
        list[from].add(to)
        list[to].add(from)
    }

    fun removeEdge(from: Int, to: Int) {
        list[from].remove(to)
        list[to].remove(from)
    }

    fun countConnections(site: Int) = list[site].size

    operator fun get(from: Int): Collection<Int> = list[from]
}