package com.dekinci.bot.game.map.graphstuff

import com.dekinci.bot.entities.River
import java.util.ArrayList

class AdjacencyList(vertexAmount: Int, rivers: List<River>) {
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
    }

    operator fun get(from: Int): Collection<Int> = list[from]
}