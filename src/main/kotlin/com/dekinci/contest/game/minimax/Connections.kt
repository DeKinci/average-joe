package com.dekinci.contest.game.minimax

import com.dekinci.contest.entities.River
import com.dekinci.contest.game.map.DistanceMetrics
import java.util.*
import kotlin.collections.HashSet

class Connections(private val allMines: Set<Int>) {
    private inner class ConnectionGroup(
            val mines: MutableSet<Int> = HashSet(),
            val sites: MutableSet<Int> = HashSet()
    ) {

        fun fits(river: River) = river.target in sites || river.source in sites

        fun add(river: River) {
            if (river.source in allMines)
                mines.add(river.source)
            if (river.target in allMines)
                mines.add(river.target)

            sites.add(river.source)
            sites.add(river.target)
        }

        fun merge(other: ConnectionGroup) {
            mines.addAll(other.mines)
            sites.addAll(other.sites)
        }
    }

    private constructor(allMines: Set<Int>, groups: Set<ConnectionGroup>) : this(allMines) {
        this.groups.addAll(groups)
    }

    private val groups = Collections.newSetFromMap(IdentityHashMap<ConnectionGroup, Boolean>())

    fun addRiver(river: River): Connections {
        val result = Connections(allMines, groups)

        if (result.groups.isEmpty()) {
            result.newGroupFrom(river)
        }

        val toMerge = result.groups.filter { it.fits(river) }
        when {
            toMerge.isEmpty() -> result.newGroupFrom(river)
            toMerge.size == 1 -> toMerge.first().add(river)
            else -> {
                val base = toMerge.first()
                toMerge.forEach {
                    if (it !== base)
                        base.merge(it)
                }
                base.add(river)
                result.groups.removeAll(toMerge)
                result.groups.add(base)
            }
        }

        return result
    }

    private fun newGroupFrom(river: River) {
        val ncg = ConnectionGroup()
        ncg.add(river)
        groups.add(ncg)
    }

    fun cost(metrics: DistanceMetrics): Int {
        var result = 0
        for (group in groups) {
            result += group.mines.sumBy { metrics.costHavingSites(it, group.sites) }
        }
        return result
    }

    override fun toString(): String {
        return groups.joinToString("\n") { "${it.mines} : ${it.sites}" } + "\n"
    }
}