package com.dekinci.bot.game.player


class Player {
    val properties = PlayerProperties()

    fun claimMine(mine: Int) {
        addSite(mine)
        properties.addMine(mine)
    }

    fun addSite(site: Int) {
        properties.addSite(site)
    }
}