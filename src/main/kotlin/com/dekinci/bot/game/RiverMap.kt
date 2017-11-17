package com.dekinci.bot.game

import com.dekinci.bot.entities.River
import com.dekinci.bot.entities.RiverState

class RiverMap(val riverMatrix: ArrayList<ArrayList<RiverState>>) {

    companion object {
        operator fun invoke(sitesAmount: Int, rivers: List<River>): RiverMap {
            val gameMap = ArrayList<ArrayList<RiverState>>(sitesAmount)

            for (row in 0 .. sitesAmount) {
                val temp = ArrayList<RiverState>(sitesAmount)

                for (cell in 0 .. sitesAmount)
                    temp.add(RiverState.Defunct)

                gameMap.add(temp)
            }

            for (river in rivers) {
                val row = river.source
                val cell = river.target
                gameMap[row][cell] = RiverState.Neutral
            }

            return RiverMap(gameMap)
        }
    }

    fun find(predicate: (River) -> Boolean): River? {
        forEachRiver { river ->
            if (predicate(river))
                return river
        }
        return null
    }

    fun findAll(predicate: (River) -> Boolean): ArrayList<River> {
        val list = ArrayList<River>()
        forEachRiver { river ->
            if (predicate(river))
                list.add(river)
        }
        return list
    }

    operator fun get(x: Int, y: Int): RiverState = riverMatrix[x][y]

    operator fun set(x: Int, y: Int, t: RiverState) {
        riverMatrix[x][y] = t
    }

    inline fun forEach(operation: (RiverState) -> Unit) = riverMatrix.forEach { it.forEach { operation.invoke(it) } }

    inline fun forEachRiver(operation: (River) -> Unit) =
            riverMatrix.forEachIndexed { x, p -> p.forEachIndexed { y, t -> operation.invoke(River(x, y, t)) } }
}