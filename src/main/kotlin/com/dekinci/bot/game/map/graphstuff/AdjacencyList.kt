package com.dekinci.bot.game.map.graphstuff

import com.dekinci.bot.entities.River
import java.util.ArrayList

class AdjacencyList(vertexAmount: Int) {
    val list: ArrayList<ArrayList<Int>> = ArrayList(vertexAmount)

    constructor(vertexAmount: Int, rivers: List<River>) : this(vertexAmount) {
        rivers.forEach { river ->
            addEdge(river.source, river.target)
        }
    }

    init {
        for (i in 0 until vertexAmount)
            list.add(ArrayList<Int>())
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