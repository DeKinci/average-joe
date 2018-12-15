package com.dekinci.contest.entities

object RiverStateID {
    /**
     * -2 - DEFUNCT - if there is no river
     * -1 - NEUTRAL - if this river is not claimed yet
     * other - ID of the player, who claimed the river
     */
    const val DEFUNCT = -2
    const val NEUTRAL = -1
}