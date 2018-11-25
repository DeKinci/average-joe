package com.dekinci.bot.entities

import ru.spbstu.competition.protocol.data.Claim
import java.lang.IllegalStateException

class StatedRiver(val source: Int, val target: Int, val state: Int = RiverStateID.NEUTRAL) {
    constructor(claim: Claim) : this(claim.source, claim.target, claim.punter)

    fun stateless() = River(source, target)

    fun has(site: Int) = source == site || target == site

    fun another(site: Int) = if (source == site) target else if (target == site) source else throw IllegalStateException("No $site")

    override fun equals(other: Any?) = (other is StatedRiver &&
            (source == other.source && target == other.target ||
                    source == other.target && target == other.source) && other.state == state) ||
            (other is River &&
                    (source == other.source && target == other.target ||
                            source == other.target && target == other.source))

    override fun hashCode() = (source.hashCode() xor target.hashCode()) + 31 * (state + 1)

    override fun toString(): String = "$source $target $state"

    fun stated(state: Int) = StatedRiver(source, target, state)
}