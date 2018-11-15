package com.dekinci.bot.game.strategy

import com.dekinci.bot.game.GameState
import com.dekinci.bot.game.map.GameMap
import com.dekinci.bot.game.tactics.ConnectMinesTactics
import com.dekinci.bot.game.tactics.PassTactics
import com.dekinci.bot.game.tactics.PathFinder
import com.dekinci.bot.game.tactics.Tactics
import com.dekinci.bot.moves.Move
import com.dekinci.bot.moves.PassMove

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

    override fun next(): Move {
        if (hasNext())
            return tactics.next()
        return PassMove()
    }

    override fun hasNext(): Boolean {
        if (tactics is PassTactics)
            return chooseTactics()
        return tactics.hasNext()
    }

    private fun chooseIsland() {
        for (isl in gameState.gameMap.islands)
            if (island == null || isl.cost > island!!.cost)
                island = isl

        println("Chosen island cost: ${island!!.cost}, mines: ${island!!.mines}")
    }

    private fun chooseTactics(): Boolean {
        val cities = island!!.mines
                .map { it to gameState.gameMap.realMetrics.mineCost(it).toDouble() }
                .sortedBy { it.second }.reversed()
        println("Chosen possible cities: $cities")

        var possibleCities = cities
                .filter { !gameState.gameMap.ourSites.contains(it.first) }

        if (possibleCities.isEmpty()) {
            tactics = PassTactics()
            println("No possible cities")
            return false
        }

        if (possibleCities.size < 2)
            possibleCities += cities.subtract(possibleCities).take(1)
        println("Chosen possible cities: $possibleCities")

        val bestCity = possibleCities.maxBy { it.second }

        if (bestCity == null) {
            println("Could not find best city")
            tactics = PassTactics()
            return false
        }

        println("best city is ${bestCity.first} with a score ${bestCity.second}")

        val fancyScore = bestCity.second * 0.9
        val fancyCities = possibleCities.filter { it.second >= fancyScore }.toMutableList()
        if (fancyCities.size < 2)
            fancyCities.add(possibleCities.subList(1, possibleCities.size).first())

        println("Chosen fancy cities: $fancyCities")

        var first = -1
        var second = -1

        var maxMetric = -1.0

        for (a in 0 until fancyCities.size)
            for (b in 0 until a) {
                val metric = (fancyCities[a].second + fancyCities[b].second) /
                        (1.1 - 1 / Math.pow(estimateK(fancyCities[a].first, fancyCities[b].first).toDouble(), 2.0))
                println("Metric for ${fancyCities[a].first} to ${fancyCities[b].first} is $metric")

                if (metric > maxMetric) {
                    println("Metric is better!")
                    maxMetric = metric
                    first = fancyCities[a].first
                    second = fancyCities[b].first
                }
            }

        println("Chosen cities: $first, $second")
        tactics = ConnectMinesTactics(gameState.gameMap, first, second)
        return true
    }

    private fun estimateK(from: Int, to: Int): Int {
        val pf = PathFinder(gameState.gameMap)

        val listPaths = ArrayList<List<Int>>()
        while (listPaths.size < 7) {
            val path = pf.findPath(from, to, listPaths)
            if (!path.isEmpty())
                listPaths.add(listOf(from) + path)
            else
                break
        }

        println("Estimated K from $from to $to is ${listPaths.size}")
        return listPaths.size
    }
}