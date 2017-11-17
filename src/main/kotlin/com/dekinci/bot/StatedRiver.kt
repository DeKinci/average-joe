package com.dekinci.bot

data class StatedRiver(val source: Int, val target: Int, val state: RiverState) {
    override fun equals(other: Any?) =
            other is StatedRiver &&
                    (source == other.source && target == other.target ||
                            source == other.target && target == other.source)

    override fun hashCode() = source.hashCode() xor target.hashCode()
}