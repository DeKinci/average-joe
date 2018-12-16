package com.dekinci.contest.game.strategy

import com.dekinci.contest.common.Log.debug
import com.dekinci.contest.common.Log.err
import com.dekinci.contest.common.Log.trace
import com.dekinci.contest.common.Log.warn
import com.dekinci.contest.entities.StatedRiver
import com.dekinci.contest.game.GameState
import com.dekinci.contest.game.map.GameMap
import com.dekinci.contest.game.tactics.ConnectMinesTactics
import com.dekinci.contest.game.tactics.PassTactics
import com.dekinci.contest.game.tactics.PathFinder
import com.dekinci.contest.game.tactics.Tactics
import kotlin.math.min

class BuildEmpireStrategy(private val gameState: GameState) : Strategy {
    private var tactics: Tactics = PassTactics()
    private var island: GameMap.Island? = null

    init {
        chooseIsland()
        chooseTactics()
    }

    override fun isFinished() = tactics is PassTactics

    override fun isSuccessful(): Boolean {
        return true
    }

    override fun next(): StatedRiver? {
        if (hasNext())
            return tactics.next()
        return null
    }

    override fun hasNext(): Boolean {
        if (tactics is PassTactics)
            return false

        var counter = 3
        while (!tactics.hasNext() && counter > 0) {
            if(!chooseTactics())
                return false
            counter--
        }

        return tactics.hasNext()
    }

    private fun chooseIsland() {
        for (isl in gameState.gameMap.islands)
            if (island == null || isl.cost > island!!.cost)
                island = isl

        debug("Chosen island cost: ${island!!.cost}, mines: ${island!!.mines}")
    }

    private fun chooseTactics(): Boolean {
        val cities = island!!.mines
                .map { it to gameState.gameMap.squareMetrics.mineCost(it).toDouble() }
                .sortedBy { it.second }.reversed()
        trace("Chosen possible cities: $cities")

        var possibleCities = cities
//                .filter { !gameState.gameMap.ourSites.contains(it.first) } TODO

        if (possibleCities.isEmpty()) {
            tactics = PassTactics()
            warn("No possible cities")
            return false
        }

        if (possibleCities.size < 2)
            possibleCities += cities.subtract(possibleCities).take(1)
        trace("Chosen possible cities: $possibleCities")

        val bestCity = possibleCities.maxBy { it.second }

        if (bestCity == null) {
            err("Could not find best city")
            tactics = PassTactics()
            return false
        }

        trace("best city is ${bestCity.first} with a score ${bestCity.second}")

        val fancyScore = bestCity.second * 0.9
        val fancyCities = possibleCities.filter { it.second >= fancyScore }.toMutableList()
        if (fancyCities.size < 2)
            fancyCities.add(possibleCities.subList(1, possibleCities.size).first())

        trace("Chosen fancy cities: $fancyCities")

        var first = -1
        var second = -1

        var maxMetric = -1.0

        for (a in 0 until fancyCities.size)
            for (b in 0 until a) {
                val metric = (fancyCities[a].second + fancyCities[b].second) /
                        (1.1 - 1 / Math.pow(estimateK(fancyCities[a].first, fancyCities[b].first).toDouble(), 2.0))
                trace("Metric for ${fancyCities[a].first} to ${fancyCities[b].first} is $metric")

                if (metric > maxMetric) {
                    maxMetric = metric
                    first = fancyCities[a].first
                    second = fancyCities[b].first
                }
            }

        debug("Chosen cities: $first, $second")
        tactics = ConnectMinesTactics(gameState.punter, gameState.gameMap, first, second)
        return true
    }

    private fun estimateK(from: Int, to: Int): Int {
        val pf = PathFinder(gameState.gameMap)

        val threshold = min(
                gameState.gameMap.getAvailableConnections(from, gameState.punter).size,
                gameState.gameMap.getAvailableConnections(to, gameState.punter).size)

        val listPaths = ArrayList<List<Int>>()
        while (listPaths.size <= threshold) {
            val path = pf.findPath(from, to, gameState.punter, listPaths)
            if (!path.isEmpty())
                listPaths.add(listOf(from) + path)
            else
                break
        }

        trace("Estimated K from $from to $to is ${listPaths.size}")
        return listPaths.size
    }
}