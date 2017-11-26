package com.dekinci.bot.entities

class RiverStateID {
    /**
     * -2 - DEFUNCT - if there is no river
     * -1 - NEUTRAL - if this river is not claimed yet
     * other - ID of the player, who claimed the river
     */

    companion object {
        val DEFUNCT = -2
        val NEUTRAL = -1
    }
}