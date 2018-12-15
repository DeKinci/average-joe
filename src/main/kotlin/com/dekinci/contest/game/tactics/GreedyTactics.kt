package com.dekinci.contest.game.tactics

import com.dekinci.contest.entities.StatedRiver
import com.dekinci.contest.game.GameState

class GreedyTactics(private val gameState: GameState) : Tactics {
    private var isFinished = false
    private var isSuccessful = false

    override fun isFinished() = isFinished

    override fun isSuccessful() = isSuccessful

    override fun next(): StatedRiver? {
        TODO()
    }

    override fun hasNext(): Boolean {
        return false
    }
}