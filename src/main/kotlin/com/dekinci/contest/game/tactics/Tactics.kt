package com.dekinci.contest.game.tactics

import com.dekinci.contest.entities.StatedRiver

/**
 * Tactics is a consequence of taking concrete points.
 * In different position contest may need different tactics
 * e.g. building mines net, spreading or aggression,
 * so tactics mechanism implements a finite-state machine
 * for choosing concrete behavior.
 */
interface Tactics : Iterator<StatedRiver?> {
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
    override fun next(): StatedRiver?

    /**
     * True if tactics has an idea to continue.
     * Usable as iterator.
     */
    override fun hasNext(): Boolean
}