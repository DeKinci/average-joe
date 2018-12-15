package com.dekinci.contest.game.strategy

import com.dekinci.contest.entities.StatedRiver

/**
 * Strategy is a decision-making mechanism of the contest.
 * It has a possibility to invoke transition of the tactics machine.
 *
 * Strategy does not says, which rivers to take, but it defines the
 * general behavior of the contest.
 * It is a finite-state machine.
 */
interface Strategy : Iterator<StatedRiver?> {

    fun isFinished(): Boolean

    fun isSuccessful(): Boolean

    override fun next(): StatedRiver?

    override fun hasNext(): Boolean
}