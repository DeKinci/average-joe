package com.dekinci.contest.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.dekinci.contest.protocol.Claim
import java.lang.IllegalStateException

@JsonIgnoreProperties(ignoreUnknown = true)
open class River(val source: Int, val target: Int) {
    constructor(claim: Claim) : this(claim.source, claim.target)

    fun has(site: Int) = source == site || target == site

    fun another(site: Int) = if (source == site) target else if (target == site) source else throw IllegalStateException("No $site")

    override fun equals(other: Any?) = other is River &&
            (source == other.source && target == other.target || source == other.target && target == other.source)

    override fun hashCode() = source.hashCode() xor target.hashCode()

    override fun toString(): String = "($source, $target)"

    fun stated(state: Int) = StatedRiver(source, target, state)
}