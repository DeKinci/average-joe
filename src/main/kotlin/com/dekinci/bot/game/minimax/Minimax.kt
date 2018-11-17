package com.dekinci.bot.game.minimax

import com.dekinci.bot.common.LayeredHashSet
import com.dekinci.bot.entities.River
import com.dekinci.bot.game.map.GameMap

class Minimax(private val playersAmount: Int, private val gameMap: GameMap) {

    private val layeredFancyRivers = LayeredHashSet<River, Int>()
    private val viewedTurns = HashSet<Turn>()
    private val matchedTurns = HashMap<Int, ArrayList<Turn>>()

    private var rootTurn = Turn.root()
    private var bestNextTurn: Turn = rootTurn
    private var id = 0

    init {
        layeredFancyRivers.addLayer(0)
        val set = layeredFancyRivers.get(0)
        gameMap.mines.forEach { siteChange(set, it) }
//        layeredFancyRivers.rotateToLayer(0)
    }

    fun findBest(depth: Int, targetPlayer: Int): River? {
        for (i in 0 until depth)
            nextLvl(i, targetPlayer)

        return bestNextTurn.firstRiverFor(targetPlayer, rootTurn)
    }

    fun update(river: River) {
        val skeleton = rootTurn.skeleton(river)
        var next = matchedTurns[skeleton.hashCode()]?.find { it == skeleton }

        if (next == null) {
            id++
            val score = calculate(rootTurn.allRiversOf(river.state), river)
            next = rootTurn.next(river, score, id)

            layeredFancyRivers.extendLayer(rootTurn.id, id)
            val fancyRivers = layeredFancyRivers.get(id)

            val takenRivers = next.allRivers()
            siteChange(fancyRivers, river.source, takenRivers)
            siteChange(fancyRivers, river.target, takenRivers)
        }
//        else
//            layeredFancyRivers.rotateToLayer(next.id)

        rootTurn = next
        bestNextTurn = Turn.root()
    }

    private fun nextLvl(lvl: Int, targetPlayer: Int): Boolean {
        var moved = false
        for (i in 0 until playersAmount)
            if (turn((targetPlayer + i) % playersAmount, lvl, targetPlayer))
                moved = true

        return moved
    }

    private fun turn(playerN: Int, lvl: Int, targetPlayer: Int): Boolean {
        var turnSet = HashSet<Turn>()
        turnSet.add(rootTurn)
        for (i in 0 until lvl) {
            val newSet = HashSet<Turn>()
            for (turn in turnSet)
                newSet.addAll(turn.siblings())
            turnSet = newSet
        }

        for (turn in turnSet) {
            if (turn !in viewedTurns) {
                for (river in layeredFancyRivers.get(turn.id)) {
                    id++
                    val cost = calculate(turn.allRiversOf(playerN), river)
                    val next = turn.next(river.changeState(playerN), cost, id)

                    if (playerN == targetPlayer && next.score > bestNextTurn.score)
                        bestNextTurn = next

                    layeredFancyRivers.extendLayer(turn.id, id)
                    val fancyRivers = layeredFancyRivers.get(id)

                    val takenRivers = next.allRivers()
                    siteChange(fancyRivers, river.source, takenRivers)
                    siteChange(fancyRivers, river.target, takenRivers)
//                    println("B $fancyRivers")
                }
                viewedTurns.add(turn)
                matchedTurns.merge(turn.hashCode(), arrayListOf(turn)) { old, new -> old.also { it.addAll(new) } }
            }
            else {
                val matched = matchedTurns[turn.hashCode()]!!.find { it == turn }
                matched?.let { turn.replaceBy(it) }
            }
        }

        return turnSet.isEmpty()
    }

    private fun siteChange(rivers: MutableSet<River>, site: Int, takenRivers: Set<River> = emptySet()) {
        val takenConnections = takenRivers.filter { it.has(site) }.map { it.another(site) }
        val connections = gameMap.getFreeConnections(site).subtract(takenConnections)

        if (connections.isEmpty())
            rivers.removeAll { it.has(site) }
        else
            rivers.addAll(connections.map { River(site, it) })
    }

    private fun calculate(rivers: Set<River>, river: River) = gameMap.realMetrics.getHavingRivers(rivers + river)
}