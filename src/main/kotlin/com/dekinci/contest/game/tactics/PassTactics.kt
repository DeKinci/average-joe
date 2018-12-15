package com.dekinci.contest.game.tactics


class PassTactics : Tactics {
    override fun isFinished() = false

    override fun isSuccessful() = false

    override fun next(): Nothing? = null

    override fun hasNext() = true
}