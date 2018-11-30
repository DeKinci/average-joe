package com.dekinci.bot.game.minimax

import com.dekinci.bot.common.WeakLayeredHashSet
import com.dekinci.bot.common.newWeakHashSet
import com.dekinci.bot.entities.River
import com.dekinci.bot.entities.StatedRiver
import com.dekinci.bot.game.map.GameMap
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

class Minimax(private val playersAmount: Int, private val gameMap: GameMap) {

    private val layeredFancyRivers = WeakLayeredHashSet<River, String>()
    private val layeredConnections = HashMap<Int, WeakLayeredHashSet<Int, String>>()

    private val adultTurns = newWeakHashSet<Turn>()
    private val matchedTurns = HashMap<Int, MutableSet<Turn>>()

    private var rootTurn = Turn.root()
    private var bestNextTurn: AtomicReference<Turn> = AtomicReference(rootTurn)
    private var idCounter = 0

    private val interrupt = AtomicBoolean(false)

    init {
        initConnections()
        initFancyRivers()
    }

    private fun initConnections() {
        gameMap.mines.forEach {
            val lhs = WeakLayeredHashSet<Int, String>()
            lhs.baseAdd(it)
            lhs.addLayer(rootTurn.id)
            lhs.rotateToLayer(rootTurn.id)
            layeredConnections[it] = lhs
        }
    }

    private fun initFancyRivers() {
        val set = layeredFancyRivers.addLayer(rootTurn.id)
        gameMap.mines.forEach { siteChange(set, it) }
        layeredFancyRivers.rotateToLayer(rootTurn.id)
    }

    fun runCycle(depth: Int, targetPlayer: Int) {
        Stat.start("best")
        interrupt.set(false)
        var depthCounter = 0

        for (i in 0 until depth) {
            depthCounter++
            if (interrupt.get())
                break
            var moved = false
            if (nextLvl(i, targetPlayer))
                moved = true
            if (!moved)
                break
        }

        println("Depth: $depthCounter")
        Stat.end("best")
    }

    fun interrupt() {
        interrupt.set(true)
    }

    fun getBest(targetPlayer: Int): StatedRiver? {
        return bestNextTurn.get().firstRiverFor(targetPlayer, rootTurn)
    }

    fun update(river: StatedRiver) {
        Stat.start("update")
        val skeleton = rootTurn.skeleton(river)

        val next = if (adultTurns.contains(skeleton)) {
            Stat.start("skeleton restoration")
            val result = matchedTurns[skeleton.hashCode()]!!.find { it == skeleton }!!
//            layeredFancyRivers.rotateToLayer(result.idCounter)
//            gameMap.mines.forEach { layeredConnections[it]!!.rotateToLayer(result.idCounter) }
            Stat.end("skeleton restoration")
            result
        } else {
            nextTurn(rootTurn, river.stateless(), river.state)
        }

        rootTurn = next
        bestNextTurn.set(Turn.root())
        Stat.end("update")
    }

    private fun nextLvl(lvl: Int, targetPlayer: Int): Boolean {
        var moved = false
//        for (i in 0 until playersAmount) {
//            if (interrupt.get())
//                break
            if (turn((targetPlayer) % playersAmount, lvl, targetPlayer))
                moved = true
//        }

        return moved
    }

    private fun turn(playerN: Int, lvl: Int, targetPlayer: Int): Boolean {
        Stat.start("turn")
        val turnSet = findNextTurns(lvl)
//        println("Size: ${turnSet.size}")
        if (interrupt.get())
            return false

        for (turn in turnSet) {
            if (interrupt.get())
                break
            if (turn !in adultTurns) {
                for (river in layeredFancyRivers.get(turn.id)) {
                    if (interrupt.get())
                        break
                    val next = nextTurn(turn, river, playerN)
                    if (playerN == targetPlayer && next.score > bestNextTurn.get().score)
                        bestNextTurn.set(next)
                }

                adultTurns.add(turn)
                val set = matchedTurns[turn.hashCode()]
                if (set != null)
                    set.add(turn)
                else
                    matchedTurns[turn.hashCode()] = newWeakHashSet(turn)
            } else {
                val matched = matchedTurns[turn.hashCode()]!!.find { it == turn }!!
                turn.replaceBy(matched)
            }
        }

        Stat.end("turn")
        return turnSet.isNotEmpty()
    }

    private fun findNextTurns(depth: Int): Set<Turn> {
        Stat.start("slice")
        var turns: Set<Turn> = HashSet<Turn>().also { it.add(rootTurn) }

        for (i in 0 until depth) {
            if (interrupt.get())
                break

            val newSet = map(turns)
//            println("Mapped size is ${turns.size}")
            if (interrupt.get())
                break
            turns = reduce(newSet)
//            println("Reduced size is ${turns.size}")
        }
        Stat.end("slice")
        return turns
    }

    private fun map(turns: Set<Turn>): Set<Turn> {
        val newSet = HashSet<Turn>()
        for (turn in turns)
            newSet.addAll(turn.siblings())
        return newSet
    }

    private fun reduce(turns: Set<Turn>): Set<Turn> {
        val best = turns.maxBy { it.score }
        return best?.let { _ -> turns.filter { best.score == it.score }.toHashSet() } ?: emptySet()
    }

    private fun nextTurn(parent: Turn, river: River, playerN: Int): Turn {
        val skeleton = parent.skeleton(river.stated(playerN))
        if (parent.siblings().contains(skeleton))
            return parent.siblings().find { it == skeleton }!!

        Stat.start("nextTurn")

        idCounter++
        val nextId = idCounter.toString()
        val idForConnections = parent.prevTurnOf(playerN).id
        val cons = HashMap<Int, Set<Int>>()
        layeredConnections.forEach {
            val connections = it.value
            connections.extendLayer(idForConnections, nextId)
            val set = connections.get(nextId)
            if (set.contains(river.target))
                set.add(river.source)
            if (set.contains(river.source))
                set.add(river.target)
            cons[it.key] = set
        }

        val cost = calculate(cons)
        val next = parent.next(river.stated(playerN), cost, nextId)

        val fancyRivers = layeredFancyRivers.extendLayer(parent.id, nextId)
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