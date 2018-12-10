package com.dekinci.bot.game.minimax

import com.dekinci.bot.common.WeakLayeredHashSet
import com.dekinci.bot.common.newWeakHashSet
import com.dekinci.bot.entities.River
import com.dekinci.bot.entities.StatedRiver
import com.dekinci.bot.game.map.GameMap
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference
import kotlin.collections.HashSet


class Minimax(private val playersAmount: Int, private val gameMap: GameMap, private val depth: Int) {

    private val layeredFancyRivers = WeakLayeredHashSet<River, String>()
    private val layeredConnections = WeakHashMap<String, Connections>()

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
        val lhs = Connections(gameMap.mines)
        layeredConnections[rootTurn.id] = lhs
    }

    private fun initFancyRivers() {
        val set = layeredFancyRivers.addLayer(rootTurn.id)
        gameMap.mines.forEach { siteChange(set, it) }
        layeredFancyRivers.rotateToLayer(rootTurn.id)
    }

    fun getBest(targetPlayer: Int): StatedRiver? {
        return bestNextTurn.get().firstTurnFor(targetPlayer, rootTurn)?.deltaRiver
    }

    fun interrupt() {
        interrupt.set(true)
    }

    fun update(river: StatedRiver) {
        Stat.start("update")
        val skeleton = rootTurn.skeleton(river)

        rootTurn = if (adultTurns.contains(skeleton))
            matchedTurns[skeleton.hashCode()]!!.find { it == skeleton }!!
        else
            nextTurn(rootTurn, river.stateless(), river.state)

        bestNextTurn.set(Turn.root())
        System.gc()
        Stat.end("update")
    }

    fun runCycle(targetPlayer: Int) {
        Stat.start("cycle")
        interrupt.set(false)
        var depthCounter = 0

        var nextTurns = setOf(rootTurn)

        for (i in 0 until depth) {
            depthCounter++
            if (interrupt.get())
                break

            nextTurns = nextRound(nextTurns, targetPlayer)

            if (nextTurns.isEmpty())
                break
        }

        println("Depth: $depthCounter")
        Stat.end("cycle")
    }

    private fun nextRound(turnSet: Set<Turn>, targetPlayer: Int): Set<Turn> {
        var turns = turnSet
        val delta = (rootTurn.deltaRiver?.state ?: -1) + 1
        for (i in 0 until playersAmount) {
            turns = turn((i + delta) % playersAmount, turns, targetPlayer)

            if (interrupt.get())
                break
        }

        return turns
    }

    private fun turn(playerN: Int, turnSet: Set<Turn>, targetPlayer: Int): Set<Turn> {
        val children = HashSet<Turn>()

        Stat.start("turn")

        rootLoop@ for (turn in turnSet) {
            if (interrupt.get())
                break

            var siblings = turn.siblings()
            if (turn !in adultTurns) {
                for (river in layeredFancyRivers.get(turn.id)) {
                    if (interrupt.get())
                        break@rootLoop

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
                println("Matched reused!")
                turn.replaceBy(matched)
                siblings = matched.siblings()
            }

            children.addAll(siblings)
        }

        Stat.end("turn")
        return reduce(children)
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
        val cons = layeredConnections[idForConnections]!!.addRiver(river)
        layeredConnections[nextId] = cons

        val next = parent.next(river.stated(playerN), cons.cost(gameMap.realMetrics), nextId)

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

    internal fun siteChange(fancyRivers: MutableSet<River>, site: Int) {
        val connections = gameMap.getFreeConnections(site)

        if (connections.isEmpty())
            fancyRivers.removeAll { it.has(site) }
        else
            fancyRivers.addAll(connections.map { River(site, it) })
    }
}