package com.dekinci.bot.game.tactics

/**
 * Stub to prevent nullability in the intellect.
 * Does nothing, but still not useless.
 */
class StubTactics : Tactics {
    override fun isFinished(): Boolean {
        throw UnsupportedOperationException("Stub invocation")
    }

    override fun isSuccessful(): Boolean {
        throw UnsupportedOperationException("Stub invocation")
    }

    override fun nextNMoves(n: Int): List<Int> {
        throw UnsupportedOperationException("Stub invocation")
    }

    override fun hasNext(): Boolean {
        throw UnsupportedOperationException("Stub invocation")
    }

    override fun next(): Int {
        throw UnsupportedOperationException("Stub invocation")
    }
}