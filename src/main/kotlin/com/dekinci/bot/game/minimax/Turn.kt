package com.dekinci.bot.game.minimax

import com.dekinci.bot.entities.StatedRiver
import java.lang.ref.SoftReference
import java.lang.ref.WeakReference

class Turn private constructor(
        val deltaRiver: StatedRiver?,
        val score: Int,
        val id: String,
        parentInit: Turn? = null
) {
    private val siblings = HashSet<Turn>()
    private val longHash: Long
    private val parent = WeakReference<Turn>(parentInit)

    private var riverSet: SoftReference<Set<StatedRiver>>? = null

    init {
        var result = 0L
        riverSet().forEach { result = result * 31 + it.hashCode() }
        longHash = result
    }

    fun next(newRiver: StatedRiver, score: Int, id: String): Turn {
        val next = Turn(newRiver, score, id, this)
        siblings.add(next)
        return next
    }

    fun replaceBy(turn: Turn) {
        parent.get()?.siblings?.remove(this)
        parent.get()?.siblings?.add(turn)
    }

    fun skeleton(newRiver: StatedRiver) = Turn(newRiver, 0, "", this)

    fun siblings(): Set<Turn> = siblings

    fun riverSet(): Set<StatedRiver> {
        if (riverSet?.get() != null)
            return riverSet!!.get()!!
        Stat.start("river set calculation")

        val set = HashSet<StatedRiver>()
        deltaRiver?.let { set.add(it) }

        var parentTurn = parent.get()
        while (parentTurn != null) {
            parentTurn.deltaRiver?.let { set.add(it) }
            parentTurn = parentTurn.parent.get()
        }

        riverSet = SoftReference(set)
        Stat.end("river set calculation")
        return set
    }

    fun firstTurnFor(player: Int, root: Turn): Turn? {
        var result : Turn? = null

        var currentTurn: Turn? = this
        while (currentTurn != null && currentTurn != root) {
            if (currentTurn.deltaRiver?.state == player)
                result = currentTurn

            currentTurn = currentTurn.parent.get()
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
            else if (parentTurn.parent.get() == null) {
                prevTurn = parentTurn
                break
            }

            parentTurn = parentTurn.parent.get()
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
        return "${parent.get()?.toString() ?: ""}; $deltaRiver"
    }

    companion object {
        fun root() = Turn(null, 0, "0", null)
    }
}