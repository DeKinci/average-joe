package com.dekinci.contest.game.minimax

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
    private val adultTurns = newWeakHashSet<Turn>()
    private val matchedTurns = HashMap<Int, MutableSet<Turn>>()

    private var rootTurn = AtomicReference<Turn>(Turn.root())
    private val ancientRiverSet = HashSet<StatedRiver>()

    private var bestNextTurn: AtomicReference<Turn> = AtomicReference(rootTurn.get())
    private var idCounter = AtomicInteger()

    private val interrupted = AtomicBoolean(false)

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

        rootTurn.get().deltaRiver?.let { ancientRiverSet.add(it) }
        if (found != null)
            rootTurn.set(found)
        else {
            println("no turn")
            val rt = rootTurn.get()
            rootTurn.set (nextTurn(rt, river.stateless(), river.state))
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

        for (i in 0 .. depth) {
            if (interrupted.get())
                break

            nextTurns = nextRound(nextTurns)
            depthCounter++

            if (nextTurns.isEmpty())
                break
        }

        println("Depth: $depthCounter")
        Stat.end("cycle")
    }

    private fun nextRound(turnSet: Set<Turn>): Set<Turn> {
        var turns = turnSet
        for (i in 0 until playersAmount) {
            if (interrupted.get())
                break
            turns = turn(turns)
            turns = reduce(turns)

            val roundBest = turns.filter { it.deltaRiver?.state == targetPlayer }.maxBy { it.score } ?: Turn.root()
            if (roundBest.score > bestNextTurn.get().score)
                bestNextTurn.set(roundBest)
        }

        return turns
    }

    private fun turn(turnSet: Set<Turn>): Set<Turn> {
        val children = HashSet<Turn>()

        Stat.start("turn")

        for (turn in turnSet) {
            if (interrupted.get())
                break

            val playerN = (turn.deltaRiver?.state ?: targetPlayer) + 1
            val siblings = findSiblings(turn, playerN % playersAmount)
            children.addAll(siblings)
        }

        Stat.end("turn")
        return children
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
        val fr = getFancyRivers(parent.riverSet() + ancientRiverSet, playerN)
        for (river in fr) {
            if (interrupted.get())
                return false

            nextTurn(parent, river, playerN)
        }
        return true
    }

    internal fun getFancyRivers(allRivers: Set<StatedRiver>, playerN: Int): Set<River> {
        val result = HashSet<River>()
        gameMap.basicMap.mines.forEach { mine ->
            gameMap.getFreeConnections(mine).forEach {
                result.add(River(mine, it))
            }
        }
        val rivers = allRivers.filter { it.state == playerN }
        rivers.forEach { river ->
            gameMap.getFreeConnections(river.source).forEach {
                result.add(River(it, river.source))
            }
            gameMap.getFreeConnections(river.target).forEach {
                result.add(River(it, river.target))
            }
        }

        return result.minus(allRivers.map { it.stateless() })
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

        val allRivers = (parent.riverSet() + ancientRiverSet).filter { it.state == playerN }.map { it.stateless() }.toHashSet()
        allRivers.add(river)
        val next = parent.next(river.stated(playerN), gameMap.squareMetrics.costHavingRivers(allRivers), nextId)

        Stat.end("nextTurn")
        return next
    }
}