package com.dekinci.contest.game.map

import com.dekinci.contest.entities.River
import java.util.*

class Cons(private val allMines: Set<Int>, rivers: Iterable<River> = emptySet()) {

    inner class Group {
        private val mines = HashSet<Int>()
        private val sites = HashSet<Int>()

        internal fun merge(other: Group) {
            mines.addAll(other.mines)
            sites.addAll(other.sites)
        }

        fun getMines(): Set<Int> = mines

        fun getSites(): Set<Int> = sites

        internal fun fits(river: River) = sites.contains(river.source) || sites.contains(river.target)

        internal fun add(river: River) {
            val source = river.source
            val target = river.target

            if (allMines.contains(source))
                mines.add(source)

            if (allMines.contains(target))
                mines.add(target)

            sites.add(river.target)
            sites.add(river.source)
        }
    }

    private val groups = Collections.newSetFromMap(HashMap<Group, Boolean>())

    init {
        rivers.forEach {
            addRiver(it)
        }
    }

    fun addRiver(river: River) {
        val toMerge = groups.filter { it.fits(river) }
        when {
            toMerge.isEmpty() -> newGroupFrom(river)
            toMerge.size == 1 -> toMerge.first().add(river)
            else -> mergeOn(river, toMerge)
        }
    }

    fun getGroups(): Set<Group> = groups

    private fun mergeOn(river: River, toMerge: List<Group>) {
        val base = toMerge.first()
        toMerge.forEach {
            if (it !== base)
                base.merge(it)
        }
        base.add(river)
        groups.removeAll(toMerge)
        groups.add(base)
    }

    private fun newGroupFrom(river: River) {
        val con = Group()
        con.add(river)
        groups.add(con)
    }
}