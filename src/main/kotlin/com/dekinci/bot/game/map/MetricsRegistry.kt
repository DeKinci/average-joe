package com.dekinci.bot.game.map

import com.dekinci.bot.game.map.graphstuff.AdjacencyList
import com.dekinci.bot.game.map.graphstuff.Dijkstra
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashSet

class MetricsRegistry(sitesAmount: Int, totalList: AdjacencyList, mines: List<Int>) {
    val weights: ConcurrentHashMap<Int, IntArray> = ConcurrentHashMap(mines.size)
    ///////////////////////////////Mine/////////Site/Weight///////////////////////////////
    var mineConnections = ConcurrentHashMap<Int, Int>(mines.size)
    var mineCost = ConcurrentHashMap<Int, Int>(mines.size)


    @Volatile
    var isReady = false

    init {
        println("creating the registry of weights...")
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

                println("finished thread with id ${Thread.currentThread().id}")
            }
        }

        threads.forEach { t ->
            t.start()
            println("starting a new thread with id = ${t.id}")
        }

        threads.forEach { t ->
            Thread({
                t.join(7000)
                if (t.isAlive)
                    println("it takes too long to wait for ${t.id}, going on")
            }).start()
        }

        Thread({
            threads.forEach(Thread::join)
            isReady = true
        }).start()
    }

    fun getForAllMines(site: Int, mines: List<Int>): Int = mines.sumBy { weights[it]?.get(site) ?: 0 }

    fun getForAllSites(mine: Int, sites: List<Int>): Int = sites.sumBy { weights[mine]?.get(it) ?: 0 }
}