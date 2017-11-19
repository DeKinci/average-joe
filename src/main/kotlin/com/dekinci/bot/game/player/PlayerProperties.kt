package com.dekinci.bot.game.player

import java.util.ArrayList

data class PlayerProperties(
        val sites: ArrayList<Int> = ArrayList(),
        val mines: ArrayList<Int> = ArrayList()
) {
    fun addMine(mine: Int) {
        mines.add(mine)
    }

    fun addSite(site: Int) {
        sites.add(site)
    }
}