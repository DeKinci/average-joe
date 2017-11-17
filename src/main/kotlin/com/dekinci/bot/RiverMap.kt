package com.dekinci.bot

import ru.spbstu.competition.protocol.data.River
import ru.spbstu.competition.protocol.data.Site

class RiverMap(val riverMatrix: ArrayList<ArrayList<RiverState>>) {

    companion object {
        operator fun invoke(sites: List<Site>, rivers: List<River>): RiverMap {
            val gameMap = ArrayList<ArrayList<RiverState>>(50)

            for (columnSite in sites) {
                val temp = ArrayList<RiverState>(50)

                for (rowSite in sites)
                    temp.add(rowSite.id, RiverState.Defunct)

                gameMap.add(columnSite.id, temp)
            }

            for (river in rivers)
                gameMap[river.source][river.target] = RiverState.Neutral

            return RiverMap(gameMap)
        }
    }

    fun find(predicate: (StatedRiver) -> Boolean): StatedRiver? {
        forEachRivered{ river ->
            if (predicate(river))
                return river
        }
        return null;
    }

    fun findAll(predicate: (StatedRiver) -> Boolean): ArrayList<StatedRiver> {
        val list = ArrayList<StatedRiver>()
        forEachRivered { river ->
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

    inline fun forEachRivered(operation: (StatedRiver) -> Unit) =
            riverMatrix.forEachIndexed { x, p -> p.forEachIndexed { y, t -> operation.invoke(StatedRiver(x, y, t)) } }
}