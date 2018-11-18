package com.dekinci.bot.game.minimax

import com.dekinci.bot.entities.StatedRiver

class Turn private constructor(
        val deltaRiver: StatedRiver?,
        val score: Int,
        val id: Int,
        val parent: Turn? = null
) {
    private val siblings = ArrayList<Turn>()

    private val hashCode: Int

    init {
        hashCode = allRivers().hashCode()
    }

    fun next(newRiver: StatedRiver, score: Int, id: Int): Turn {
        val next = Turn(newRiver, score, id, this)
        siblings.add(next)
        return next
    }

    fun replaceBy(turn: Turn) {
        val brothers = parent?.siblings
        brothers?.let { brothers[brothers.indexOf(this)] = turn }
    }

    fun skeleton(newRiver: StatedRiver) = Turn(newRiver, -1, -1, this)

    fun siblings(): List<Turn> = siblings

    fun allRivers(): Set<StatedRiver> {
        val set = HashSet<StatedRiver>()
        deltaRiver?.let { set.add(it) }

        var parentTurn = parent
        while (parentTurn != null) {
            parentTurn.deltaRiver?.let { set.add(it) }
            parentTurn = parentTurn.parent
        }

        return set
    }

    fun allRiversOf(player: Int): Set<StatedRiver> {
        val set = HashSet<StatedRiver>()
        deltaRiver?.let { set.add(it) }

        var parentTurn = parent
        while (parentTurn != null) {
            if (parentTurn.deltaRiver?.state == player)
                set.add(parentTurn.deltaRiver!!)
            parentTurn = parentTurn.parent
        }

        return set
    }

    fun firstRiverFor(player: Int, root: Turn): StatedRiver? {
        if (this == root)
            return null

        var result : StatedRiver? = deltaRiver

        var parentTurn = parent
        while (parentTurn != null && parentTurn != root) {
            if (parentTurn.deltaRiver?.state == player)
                result = parentTurn.deltaRiver

            parentTurn = parentTurn.parent
        }

        return result
    }

    override fun equals(other: Any?): Boolean {
        return other is Turn && allRivers() == other.allRivers()
    }

    override fun hashCode(): Int {
        return hashCode
    }

    override fun toString(): String {
        return "${parent?.toString() ?: ""}; $deltaRiver"
    }

    companion object {
        fun root() = Turn(null, -1, 0, null)
    }
}