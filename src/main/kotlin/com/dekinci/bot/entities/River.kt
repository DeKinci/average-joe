package com.dekinci.bot.entities

import ru.spbstu.competition.protocol.data.Claim

data class River(var source: Int, var target: Int, val state: RiverState = RiverState.Neutral) {
    constructor(claim: Claim, state: RiverState): this(claim.source, claim.target, state)

    override fun equals(other: Any?) = other is River &&
                    (source == other.source && target == other.target ||
                            source == other.target && target == other.source)

    override fun hashCode() = source.hashCode() xor target.hashCode()

    override fun toString(): String = "$source $target $state"
}