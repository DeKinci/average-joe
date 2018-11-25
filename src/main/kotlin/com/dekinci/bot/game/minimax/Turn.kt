package com.dekinci.bot.game.minimax

import com.dekinci.bot.entities.StatedRiver

class Turn private constructor(
        val deltaRiver: StatedRiver?,
        val score: Int,
        val id: Int,
        val parent: Turn? = null
) {
    private val siblings = HashSet<Turn>()

    private val longHash: Long

    init {
        var result = 0L
        riverSet().forEach { result += 31 * it.hashCode() }
        longHash = result
    }

    fun next(newRiver: StatedRiver, score: Int, id: Int): Turn {
        val next = Turn(newRiver, score, id, this)
        siblings.add(next)
        return next
    }

    fun replaceBy(turn: Turn) {
        parent?.siblings?.remove(this)
        parent?.siblings?.add(turn)
    }

    fun skeleton(newRiver: StatedRiver) = Turn(newRiver, -1, -1, this)

    fun siblings(): Set<Turn> = siblings

    fun riverSet(): Set<StatedRiver> {
        Stat.start("river set calculation")

        val set = HashSet<StatedRiver>()
        deltaRiver?.let { set.add(it) }

        var parentTurn = parent
        while (parentTurn != null) {
            parentTurn.deltaRiver?.let { set.add(it) }
            parentTurn = parentTurn.parent
        }

        Stat.end("river set calculation")
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

    fun prevTurnOf(player: Int): Turn {
        var prevTurn: Turn = root()
        var parentTurn: Turn? = this

        while (parentTurn != null) {
            if (parentTurn.deltaRiver?.state == player) {
                prevTurn = parentTurn
                break
            }
            else if (parentTurn.parent == null) {
                prevTurn = parentTurn
                break
            }

            parentTurn = parentTurn.parent
        }

        return prevTurn
    }

    override fun equals(other: Any?): Boolean {
        if (other === this)
            return true

        return other is Turn && longHash == other.longHash && riverSet() == other.riverSet()
    }

    override fun hashCode(): Int {
        return longHash.hashCode()
    }

    override fun toString(): String {
        return "${parent?.toString() ?: ""}; $deltaRiver"
    }

    companion object {
        fun root() = Turn(null, -1, 0, null)
    }
}