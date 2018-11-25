package com.dekinci.bot.game.minimax

import com.dekinci.bot.common.LayeredHashSet
import com.dekinci.bot.entities.River
import com.dekinci.bot.entities.StatedRiver
import com.dekinci.bot.game.map.GameMap

class Minimax(private val playersAmount: Int, private val gameMap: GameMap) {

    private val layeredFancyRivers = LayeredHashSet<River, Int>()
    private val layeredConnections = HashMap<Int, LayeredHashSet<Int, Int>>()

    private val adultTurns = HashSet<Turn>()
    private val matchedTurns = HashMap<Int, ArrayList<Turn>>()

    private var rootTurn = Turn.root()
    private var bestNextTurn: Turn = rootTurn
    private var id = 0

    init {
        initConnections()
        initFancyRivers()
    }

    private fun initConnections() {
        gameMap.mines.forEach {
            val lhs = LayeredHashSet<Int, Int>()
            lhs.baseAdd(it)
            layeredConnections[it] = lhs
            lhs.addLayer(0)
            lhs.rotateToLayer(0)
        }
    }

    private fun initFancyRivers() {
        layeredFancyRivers.addLayer(0)
        val set = layeredFancyRivers.get(0)
        gameMap.mines.forEach { siteChange(set, it) }
        layeredFancyRivers.rotateToLayer(0)
    }

    fun findBest(depth: Int, targetPlayer: Int): StatedRiver? {
        Stat.start("best")
        var moved = false
        for (i in 0 until depth)
            if (nextLvl(i, targetPlayer))
                moved = true

        val result = if (moved) bestNextTurn.firstRiverFor(targetPlayer, rootTurn) else null
        Stat.end("best")
        return result
    }

    fun update(river: StatedRiver) {
        Stat.start("update")
        val skeleton = rootTurn.skeleton(river)

        val next = if (adultTurns.contains(skeleton)) {
            Stat.start("skeleton restoration")
            val result = matchedTurns[skeleton.hashCode()]!!.find { it == skeleton }!!
//            layeredFancyRivers.rotateToLayer(result.id)
//            gameMap.mines.forEach { layeredConnections[it]!!.rotateToLayer(result.id) }
            Stat.end("skeleton restoration")
            result
        } else {
            nextTurn(rootTurn, river.stateless(), river.state)
        }

        rootTurn = next
        bestNextTurn = Turn.root()
        Stat.end("update")
    }

    private fun nextLvl(lvl: Int, targetPlayer: Int): Boolean {
        var moved = false
        for (i in 0 until playersAmount)
            if (turn((targetPlayer + i) % playersAmount, lvl, targetPlayer))
                moved = true

        return moved
    }

    private fun turn(playerN: Int, lvl: Int, targetPlayer: Int): Boolean {
        Stat.start("turn")
        val turnSet = generateTurnSlice(lvl)

        for (turn in turnSet) {
            if (turn !in adultTurns) {
                for (river in layeredFancyRivers.get(turn.id)) {
                    val next = nextTurn(turn, river, playerN)
                    if (playerN == targetPlayer && next.score > bestNextTurn.score)
                        bestNextTurn = next
                }

                adultTurns.add(turn)
                matchedTurns.merge(turn.hashCode(), arrayListOf(turn)) { old, new -> old.also { it.addAll(new) } }
            } else {
                val matched = matchedTurns[turn.hashCode()]!!.find { it == turn }!!
                turn.replaceBy(matched)
            }
        }

        Stat.end("turn")
        return turnSet.isNotEmpty()
    }

    private fun generateTurnSlice(lvl: Int): Set<Turn> {
        Stat.start("slice")
        var turnSet = HashSet<Turn>()
        turnSet.add(rootTurn)
        for (i in 0 until lvl) {
            val newSet = HashSet<Turn>()
            for (turn in turnSet)
                newSet.addAll(turn.siblings())

//            println("Slice for $lvl-$i lvl is ${turnSet.size}")
            turnSet = newSet.sortedBy { it.score }.takeLast(20).toHashSet()
        }
//        println("Final slice: ${turnSet.size}")
        Stat.end("slice")
        return turnSet
    }

    private fun nextTurn(parent: Turn, river: River, playerN: Int): Turn {
        val skeleton = parent.skeleton(river.stated(playerN))
        if (parent.siblings().contains(skeleton))
            return parent.siblings().find { it == skeleton }!!

        Stat.start("nextTurn")

        id++
        val idForConnections = parent.prevTurnOf(playerN).id
        val cons = HashMap<Int, Set<Int>>()
        layeredConnections.forEach {
            val connections = it.value
            connections.extendLayer(idForConnections, id)
            val set = connections.get(id)
            if (set.contains(river.target))
                set.add(river.source)
            if (set.contains(river.source))
                set.add(river.target)
            cons[it.key] = set
        }

        val cost = calculate(cons)
        val next = parent.next(river.stated(playerN), cost, id)

        layeredFancyRivers.extendLayer(parent.id, id)
        val fancyRivers = layeredFancyRivers.get(id)
        riverChange(fancyRivers, river)
        Stat.end("nextTurn")
        return next
    }

    internal fun riverChange(fancyRivers: MutableSet<River>, river: River) {
        siteChange(fancyRivers, river.source)
        siteChange(fancyRivers, river.target)
        fancyRivers.remove(river)
    }

    fun siteChange(fancyRivers: MutableSet<River>, site: Int) {
        val connections = gameMap.getFreeConnections(site)

        if (connections.isEmpty())
            fancyRivers.removeAll { it.has(site) }
        else
            fancyRivers.addAll(connections.map { River(site, it) })
    }

    private fun calculate(connected: Map<Int, Set<Int>>): Int {
        return connected.entries.sumBy { gameMap.realMetrics.getForAllSites(it.key, it.value) }
    }
}