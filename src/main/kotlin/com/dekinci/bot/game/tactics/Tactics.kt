package com.dekinci.bot.game.tactics

import com.dekinci.bot.moves.Move

/**
 * Tactics is a consequence of taking concrete points.
 * In different position bot may need different tactics
 * e.g. building mines net, spreading or aggression,
 * so tactics mechanism implements a finite-state machine
 * for choosing concrete behavior.
 */
interface Tactics : Iterator<Move> {
    /**
     * Returns true if tactics finished (WAS NOT INTERRUPTED).
     */
    fun isFinished(): Boolean

    /**
     * Returns true if tactics has a possibility to succeed.
     */
    fun isSuccessful(): Boolean

    /**
     * Updates tactics.
     * Returns next step of tactics.
     * Usable as iterator.
     */
    override fun next(): Move

    /**
     * True if tactics has an idea to continue.
     * Usable as iterator.
     */
    override fun hasNext(): Boolean
}