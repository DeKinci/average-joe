package com.dekinci.bot.game.strategy

interface Strategy: Iterator<Int> {
    fun isFinished(): Boolean
    fun isSuccessful(): Boolean

    fun nextNMoves(n: Int): List<Int>
}