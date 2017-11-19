package com.dekinci.bot.game.map

import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashSet

class WeightsRegistry(val weights: ConcurrentHashMap<Int, SortedMap<Int, Int>>) {

    companion object {
        operator fun invoke(
                matrix: AdjacencyMatrix,
                mines: List<Int>
        ): WeightsRegistry {
            println("creating the registry of weights...")
            val weights = ConcurrentHashMap<Int, SortedMap<Int, Int>>(matrix.size)
            val threads = HashSet<Thread>()

            mines.mapTo(threads) {
                Thread({
                    println("started thread for mine $it")

                    fun dfs(site: Int, counter: Int) {
                        val possibleValue = weights[site]?.get(it)
                        if (possibleValue != null && possibleValue <= counter)
                            return

                        if (weights[site] == null)
                            weights.put(site, sortedMapOf(Pair(it, counter)))
                        else if (weights[site]!![it] == null)
                            weights[site]!!.put(it, counter)

                        for (i in matrix.getConnections(site))
                            dfs(i, counter + 1)
                    }

                    dfs(it, 0)

                    println("finished thread for mine $it")
                })
            }

            threads.forEach { t ->
                t.start()
                println("starting a new thread with id = ${t.id}")
            }

            threads.forEach { t ->
                Thread({
                    t.join(9000)
                    if (t.isAlive)
                        println("it takes too long 2 w8 4 ${t.id}, going on")
                }).start()
                println("Join request for thread ${t.id}")
            }

            return WeightsRegistry(weights)
        }
    }

    fun getAllForMines(site: Int, mines: List<Int>): Int = mines.sumBy { weights[site]?.get(it) ?: 0 }
}