package com.dekinci.contest.game.strategy

class PatheticStrategy : Strategy {
    override fun isFinished() = false

    override fun isSuccessful() = false

    override fun next(): Nothing? = null

    override fun hasNext() = true
}