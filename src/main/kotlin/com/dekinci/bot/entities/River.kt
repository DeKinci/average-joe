package com.dekinci.bot.entities

import ru.spbstu.competition.protocol.data.Claim
import java.lang.IllegalStateException
import java.lang.RuntimeException

data class River(var source: Int, var target: Int, val state: Int = RiverStateID.NEUTRAL) {
    constructor(claim: Claim, state: Int): this(claim.source, claim.target, state)

    fun has(site: Int) = source == site || target == site

    fun another(site: Int) = if (source == site) target else if (target == site) source else throw IllegalStateException("No $site")

    override fun equals(other: Any?) = other is River &&
                    (source == other.source && target == other.target ||
                            source == other.target && target == other.source)

    override fun hashCode() = source.hashCode() xor target.hashCode() + 31 * state

    override fun toString(): String = "$source $target $state"

    fun changeState(state: Int) = River(source, target, state)
}