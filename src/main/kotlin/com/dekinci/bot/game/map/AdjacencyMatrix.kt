package com.dekinci.bot.game.map

import com.dekinci.bot.entities.River
import com.dekinci.bot.entities.RiverState

class AdjacencyMatrix private constructor(
        val riverMatrix: Matrix<RiverState>,
        val size: Int = riverMatrix.size
) {

    companion object {
        operator fun invoke(sitesAmount: Int, rivers: List<River>): AdjacencyMatrix {
            println("creating an adj matrix with size of $sitesAmount...")
            val gameMap = Matrix<RiverState>(sitesAmount, { _, _ -> RiverState.Defunct })

            println("matrix created, filling it with rivers...")
            for (river in rivers) {
                val row = river.source
                val cell = river.target
                gameMap[row, cell] = RiverState.Neutral
            }

            return AdjacencyMatrix(gameMap)
        }
    }

    fun getConnections(site: Int): ArrayList<Int> {
        val list = ArrayList<Int>()

        for (i in 0 until size) {
            if (riverMatrix[site, i] != RiverState.Defunct)
                list.add(i)
            if (riverMatrix[i, site] != RiverState.Defunct)
                list.add(i)
        }

        return list
    }

    fun find(predicate: (River) -> Boolean): River? {
        riverMatrix.forEachIndexed { x, y, state ->
            val river = River(x, y, state)
            if (predicate(river))
                return river
        }
        return null
    }

    fun findAll(predicate: (River) -> Boolean): ArrayList<River> {
        val list = ArrayList<River>()
        riverMatrix.forEachIndexed { x, y, state ->
            val river = River(x, y, state)
            if (predicate(river))
                list.add(river)
        }
        return list
    }

    fun hasFreeConnections(site: Int): Boolean {
        for (i in 0 until size) {
            if (riverMatrix[site, i] == RiverState.Neutral)
                return true
            if (riverMatrix[i, site] == RiverState.Neutral)
                return true
        }

        return false
    }
}