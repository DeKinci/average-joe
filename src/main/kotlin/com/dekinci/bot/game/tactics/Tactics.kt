package com.dekinci.bot.game.tactics

/**
 * Tactics is a consequence of taking concrete points.
 * In different position bot may need different tactics
 * e.g. building mines net, spreading or aggression,
 * so tactics mechanism implements a finite-state machine
 * for choosing concrete behavior.
 */
interface Tactics : Iterator<Int> {
    /**
     * Returns true if tactics finished (WAS NOT INTERRUPTED).
     */
    fun isFinished(): Boolean

    /**
     * Returns true if tactics has a possibility to succeed.
     */
    fun isSuccessful(): Boolean

    /**
     * DOES NOT UPDATES STRATEGY!!!
     * Has only one element: -1 if tactics is not successful.
     * Has no elements if tactics is finished
     * Otherwise returns n or all (if all is less) steps to success.
     */
    fun nextNMoves(n: Int): List<Int>

    /**
     * Updates tactics.
     * Returns next step of tactics.
     * Usable as iterator.
     */
    override fun next(): Int

    /**
     * True if tactics has an idea to continue.
     * Usable as iterator.
     */
    override fun hasNext(): Boolean
}