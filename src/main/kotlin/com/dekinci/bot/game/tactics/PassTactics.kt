package com.dekinci.bot.game.tactics

import com.dekinci.bot.moves.Move
import com.dekinci.bot.moves.PassMove


class PassTactics : Tactics {
    override fun isFinished() = false

    override fun isSuccessful() = false

    override fun next(): Move {
        return PassMove()
    }

    override fun hasNext() = true
}