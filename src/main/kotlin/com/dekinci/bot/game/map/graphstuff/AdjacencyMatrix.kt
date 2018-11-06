package com.dekinci.bot.game.map.graphstuff

import com.dekinci.bot.entities.River
import com.dekinci.bot.entities.RiverStateID
import java.util.HashSet

class AdjacencyMatrix (private val size: Int, rivers: List<River>) {
    private val matrix = Array(size) { IntArray(size) }

    init {
        for (river in rivers) {
            val row = river.source
            val cell = river.target
            matrix[row][cell] = RiverStateID.NEUTRAL
        }
        println("adj matrix created and filled with rivers...")
    }

    fun getConnections(site: Int): Collection<Int> {
        val connections = HashSet<Int>()

        for (i in 0 until matrix.size) {
            if (matrix[site][i] != RiverStateID.DEFUNCT)
                connections.add(i)
            if (matrix[i][site] != RiverStateID.DEFUNCT)
                connections.add(i)
        }

        return connections
    }

    fun hasFreeConnections(site: Int): Boolean {
        for (i in 0 until size) {
            if (matrix[site][i] == RiverStateID.NEUTRAL)
                return true
            if (matrix[i][site] == RiverStateID.NEUTRAL)
                return true
        }

        return false
    }

    operator fun set(from: Int, to: Int, state: Int) {
        matrix[from][to] = state
    }

    operator fun get(from: Int, to: Int): Int = matrix[from][to]
}