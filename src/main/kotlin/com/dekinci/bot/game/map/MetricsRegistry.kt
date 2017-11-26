package com.dekinci.bot.game.map

import com.dekinci.bot.entities.Site
import com.dekinci.bot.game.map.graphstuff.AdjacencyList
import com.dekinci.bot.game.map.graphstuff.Dijkstra
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashSet

class MetricsRegistry(sitesAmount: Int, totalList: AdjacencyList, mines: List<Int>) {
    val weights: ConcurrentHashMap<Int, SortedMap<Int, Int>> = ConcurrentHashMap(sitesAmount)
    ///////////////////////////////Site///////////Mine//Weight///////////////////////////////

    init {
        println("creating the registry of weights...")
        val threads = HashSet<Thread>()
        Dijkstra.init(sitesAmount, totalList.list)

        for (i in 0 until sitesAmount)
            weights.put(i, TreeMap())

        mines.mapTo(threads) {
            Thread({
                val weights = Dijkstra().sparse(it)

                weights.forEachIndexed { site, weight ->
                    this.weights[site]!!.put(it, weight * weight)
                }

                println("finished thread with id ${Thread.currentThread().id}")
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
                    println("it takes too long to wait for ${t.id}, going on")
            }).start()
        }
    }

    fun getForAllMines(site: Int, mines: List<Int>): Int = mines.sumBy { weights[site]?.get(it) ?: 0 }

    fun getForAllMinesBySites(site: Int, mines: List<Site>): Int = mines.sumBy { weights[site]?.get(it.getID()) ?: 0 }

    fun getForAllSites(mine: Int, sites: List<Int>): Int = sites.sumBy { weights[it]?.get(mine) ?: 0 }

    fun getForAllSitesBySites(mine: Int, sites: List<Site>): Int = sites.sumBy { weights[it.getID()]?.get(mine) ?: 0 }
}