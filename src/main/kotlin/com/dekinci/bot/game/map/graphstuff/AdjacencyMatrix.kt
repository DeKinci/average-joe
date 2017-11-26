package com.dekinci.bot.game.map.graphstuff

import com.dekinci.bot.entities.River
import com.dekinci.bot.entities.RiverStateID

class AdjacencyMatrix (private val size: Int, rivers: List<River>) {
    val matrix: MatrixAdapter = MatrixAdapter(size)

    init {
        for (river in rivers) {
            val row = river.source
            val cell = river.target
            matrix[row, cell] = RiverStateID.NEUTRAL
        }
        println("adj matrix created and filled with rivers...")
    }

    fun getConnections(site: Int): ArrayList<Int> {
        val list = ArrayList<Int>()

        for (i in 0 until matrix.size) {
            if (matrix[site, i] != RiverStateID.DEFUNCT)
                list.add(i)
            if (matrix[i, site] != RiverStateID.DEFUNCT)
                list.add(i)
        }

        return list
    }

    fun hasFreeConnections(site: Int): Boolean {
        for (i in 0 until size) {
            if (matrix[site, i] == RiverStateID.NEUTRAL)
                return true
            if (matrix[i, site] == RiverStateID.NEUTRAL)
                return true
        }

        return false
    }
}