package com.dekinci.bot.game.map

import com.dekinci.bot.game.map.graphstuff.AdjacencyList
import com.dekinci.bot.game.map.graphstuff.Dijkstra
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashSet

class MetricsRegistry(sitesAmount: Int, totalList: AdjacencyList, mines: List<Int>) {
    private val weights: ConcurrentHashMap<Int, IntArray> = ConcurrentHashMap(mines.size)
    ///////////////////////////////Mine/Site/Weight///////////////////////////////
    var mineConnections = ConcurrentHashMap<Int, Int>(mines.size)
    var mineCost = ConcurrentHashMap<Int, Int>(mines.size)


    @Volatile
    var isReady = false

    init {
        val threads = HashSet<Thread>(mines.size)
        Dijkstra.init(sitesAmount, totalList.list)

        mines.mapTo(threads) {
            Thread {
                weights.put(it, IntArray(sitesAmount, { _ -> -1 }))
                mineConnections.put(it, 0)
                mineCost.put(it, 0)

                Dijkstra().sparse(it)
                        .forEachIndexed { site, weight ->
                            if (weight != -1) {
                                weights[it]!![site] = weight * weight
                                mineCost[it] = mineCost[it]!! + weight * weight
                                mineConnections[it] = mineConnections[it]!! + 1
                            }
                        }

                //println("finished thread with id ${Thread.currentThread().id}")
            }
        }

        threads.forEach { t ->
            t.start()
            //println("starting a new thread with id = ${t.id}")
        }

        threads.forEach(Thread::join)
        isReady = true
        println("metrics created")
    }

    fun getForAllMines(site: Int, mines: Collection<Int>): Int = mines.sumBy { weights[it]?.get(site) ?: 0 }

    fun getForAllSites(mine: Int, sites: Collection<Int>): Int = sites.sumBy { weights[mine]?.get(it) ?: 0 }

    operator fun get(mine: Int, site: Int) = weights[mine]!![site]
}