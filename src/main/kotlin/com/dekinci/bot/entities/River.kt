package com.dekinci.bot.entities

data class River(var source: Int, var target: Int, val state: RiverState = RiverState.Neutral) {
//    init {
//        val temp = maxOf(source, target)
//        source = minOf(source, target)
//        target = temp
//    }

    override fun equals(other: Any?) =
            other is River &&
                    (source == other.source && target == other.target ||
                            source == other.target && target == other.source)

    override fun hashCode() = source.hashCode() xor target.hashCode()

    override fun toString(): String = "$source $target $state"
}