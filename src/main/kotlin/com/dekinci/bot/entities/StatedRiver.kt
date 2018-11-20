package com.dekinci.bot.entities

import ru.spbstu.competition.protocol.data.Claim
import java.lang.IllegalStateException

class StatedRiver(source: Int, target: Int, val state: Int = RiverStateID.NEUTRAL) : River(source, target) {
    constructor(claim: Claim) : this(claim.source, claim.target, claim.punter)

    fun stateless() = River(source, target)

    override fun equals(other: Any?) = (other is StatedRiver &&
            (source == other.source && target == other.target ||
                    source == other.target && target == other.source) && other.state == state) ||
            (other is River &&
                    (source == other.source && target == other.target ||
                            source == other.target && target == other.source))

    override fun hashCode() = (source.hashCode() xor target.hashCode()) + 31 * (state + 1)

    override fun toString(): String = "$source $target $state"
}