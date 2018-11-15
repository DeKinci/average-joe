package com.dekinci.bot.game.strategy

import com.dekinci.bot.moves.Move

/**
 * Strategy is a decision-making mechanism of the bot.
 * It has a possibility to invoke transition of the tactics machine.
 *
 * Strategy does not says, which rivers to take, but it defines the
 * general behavior of the bot.
 * It is a finite-state machine.
 */
interface Strategy : Iterator<Move> {

    fun isFinished(): Boolean

    fun isSuccessful(): Boolean

    override fun next(): Move

    override fun hasNext(): Boolean
}