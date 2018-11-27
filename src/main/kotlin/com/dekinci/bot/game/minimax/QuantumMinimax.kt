package com.dekinci.bot.game.minimax

import com.dekinci.bot.entities.StatedRiver

class QuantumMinimax {
    private class QTurn(val deltaRiver: StatedRiver?, val score: Int, val depth: Int) {
        override fun equals(other: Any?): Boolean {
            return other === this || (other is QTurn && other.deltaRiver == deltaRiver && other.score == score && other.depth == depth)
        }

        override fun hashCode(): Int {
            return deltaRiver.hashCode() * 31 * 31 + score.hashCode() * 31 + depth
        }

        override fun toString(): String {
            return "$deltaRiver $score $depth"
        }
    }
}