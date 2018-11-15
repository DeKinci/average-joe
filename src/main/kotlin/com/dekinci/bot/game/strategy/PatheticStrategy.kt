package com.dekinci.bot.game.strategy

import com.dekinci.bot.moves.PassMove

class PatheticStrategy : Strategy {
    override fun isFinished() = false

    override fun isSuccessful() = false

    override fun next() = PassMove()

    override fun hasNext() = true
}