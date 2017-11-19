package com.dekinci.bot.game.scoring

import com.dekinci.bot.game.map.WeightsRegistry
import com.dekinci.bot.game.player.Player

class SitesManager(private val registry: WeightsRegistry) {
    val bot = Player()
    val enemy = Player()

    var ourPoints = 0
    var enemyPoints = 0

    fun addMine(mine: Int, side: Side) {
        when (side) {
            Side.OUR -> {
                bot.claimMine(mine)
                recountUs()
            }
            Side.ENEMY -> {
                enemy.claimMine(mine)
                recountEnemy()
            }
        }
    }

    fun addSite(site: Int, side: Side) {
        when (side) {
            Side.OUR -> {
                bot.addSite(site)
                ourPoints *= 2
            }
        }
    }

    fun countSiteOur(site: Int) {
        ourPoints *= 2
        ourPoints
    }

    fun recountUs() {
        ourPoints = bot.properties.sites.sumBy { registry.getAllForMines(it, bot.properties.mines) }
    }

    fun recountEnemy() {
        enemyPoints = enemy.properties.sites.sumBy { registry.getAllForMines(it, enemy.properties.mines) }
    }
}