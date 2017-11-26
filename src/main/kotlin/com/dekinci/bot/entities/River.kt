package com.dekinci.bot.entities

import ru.spbstu.competition.protocol.data.Claim

data class River(var source: Int, var target: Int, val state: Int = RiverStateID.NEUTRAL) {
    constructor(claim: Claim, state: Int): this(claim.source, claim.target, state)

    override fun equals(other: Any?) = other is River &&
                    (source == other.source && target == other.target ||
                            source == other.target && target == other.source)

    override fun hashCode() = source.hashCode() xor target.hashCode()

    override fun toString(): String = "$source $target $state"
}