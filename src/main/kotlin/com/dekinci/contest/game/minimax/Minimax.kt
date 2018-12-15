package com.dekinci.contest.game.minimax

import com.dekinci.contest.common.WeakLayeredHashSet
import com.dekinci.contest.common.newWeakHashSet
import com.dekinci.contest.entities.River
import com.dekinci.contest.entities.StatedRiver
import com.dekinci.contest.game.map.GameMap
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import kotlin.collections.HashSet


class Minimax(
        private val playersAmount: Int,
        private val gameMap: GameMap,
        private val targetPlayer: Int,
        private val depth: Int
) {
    private val layeredFancyRivers = WeakLayeredHashSet<River, String>()

    private val adultTurns = newWeakHashSet<Turn>()
    private val matchedTurns = HashMap<Int, MutableSet<Turn>>()

    private var rootTurn = AtomicReference<Turn>(Turn.root())
    private var bestNextTurn: AtomicReference<Turn> = AtomicReference(rootTurn.get())
    private var idCounter = AtomicInteger()

    private val interrupted = AtomicBoolean(false)

    init {
        initFancyRivers()
    }

    private fun initFancyRivers() {
        val set = layeredFancyRivers.addLayer(rootTurn.get().id)
        gameMap.basicMap.mines.forEach { siteChange(set, it) }
        layeredFancyRivers.rotateToLayer(rootTurn.get().id)
    }

    fun getBest(targetPlayer: Int): StatedRiver? {
        return bestNextTurn.get().firstTurnFor(targetPlayer, rootTurn.get())?.deltaRiver
    }

    fun interrupt() {
        interrupted.set(true)
    }

    fun update(river: StatedRiver) {
        Stat.start("update")
        interrupted.set(false)
        val skeleton = rootTurn.get().skeleton(river)
        val found = findBySkeleton(skeleton)

        if (found != null)
            rootTurn.set(found)
        else {
            println("no turn")
            rootTurn.getAndUpdate {
                nextTurn(it, river.stateless(), river.state)
            }
        }

        bestNextTurn.set(Turn.root())
        System.gc()
        Stat.end("update")
    }

    private fun findBySkeleton(skeleton: Turn): Turn? = if (adultTurns.contains(skeleton))
        matchedTurns[skeleton.hashCode()]!!.find { it == skeleton }!!
    else null

    fun mineSolution() {
        Stat.start("cycle")
        var depthCounter = 0

        var nextTurns = setOf(rootTurn.get())

        for (i in 0 until depth) {
            if (interrupted.get())
                break

            depthCounter++

            nextTurns = nextRound(nextTurns)

            if (nextTurns.isEmpty())
                break
        }

        println("Depth: $depthCounter")
        Stat.end("cycle")
    }

    private fun nextRound(turnSet: Set<Turn>): Set<Turn> {
        var turns = turnSet
        val delta = (rootTurn.get().deltaRiver?.state ?: -1) + 1
        for (i in 0 until playersAmount) {
            if (interrupted.get())
                break
            turns = turn((i + delta) % playersAmount, turns)
        }

        return turns
    }

    private fun turn(playerN: Int, turnSet: Set<Turn>): Set<Turn> {
        val children = HashSet<Turn>()

        Stat.start("turn")

        for (turn in turnSet) {
            if (interrupted.get())
                break

            val siblings = findSiblings(turn, playerN)
            children.addAll(siblings)
        }

        Stat.end("turn")
        return reduce(children)
    }

    private fun findSiblings(turn: Turn, playerN: Int): Set<Turn> {
        return if (turn !in adultTurns) {
            if (doSiblingsAndFindBest(turn, playerN))
                makeAdult(turn)
            turn.siblings()
        } else {
            val matched = findBySkeleton(turn)!!
            turn.replaceBy(matched)
            matched.siblings()
        }
    }

    private fun makeAdult(turn: Turn) {
        adultTurns.add(turn)
        matchedTurns.getOrPut(turn.hashCode()) { newWeakHashSet() }.add(turn)
    }

    private fun doSiblingsAndFindBest(parent: Turn, playerN: Int): Boolean {
        for (river in layeredFancyRivers.get(parent.id)) {
            if (interrupted.get())
                return false

            val next = nextTurn(parent, river, playerN)
            if (playerN == targetPlayer && next.score > bestNextTurn.get().score)
                bestNextTurn.set(next)
        }
        return true
    }

    private fun getFancyRivers(turn: Turn, playerN: Int): Set<River> {
        val result = HashSet<River>()
        gameMap.basicMap.mines.forEach { mine ->
            gameMap.getFreeConnections(mine).forEach {
                result.add(River(mine, it))
            }
        }
        val rivers = turn.riverSet().filter { it.state == playerN }
        rivers.forEach { river ->
            gameMap.getFreeConnections(river.source).forEach {
                result.add(River(it, river.source))
            }
            gameMap.getFreeConnections(river.target).forEach {
                result.add(River(it, river.target))
            }
        }

        return result
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

        val nextId = idCounter.incrementAndGet().toString()

        val allRivers = parent.riverSet().filter { it.state == playerN }.map { it.stateless() }.toHashSet()
        allRivers.add(river)
        val next = parent.next(river.stated(playerN), gameMap.realMetrics.costHavingRivers(allRivers), nextId)

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