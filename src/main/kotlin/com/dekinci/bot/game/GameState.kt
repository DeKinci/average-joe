package com.dekinci.bot.game

import com.dekinci.bot.entities.BasicMap
import com.dekinci.bot.entities.StatedRiver
import com.dekinci.bot.game.map.GameMap
import ru.spbstu.competition.protocol.data.Claim

class GameState(val punter: Int, val playersAmount: Int, basicMap: BasicMap) {
    val gameMap = GameMap(basicMap)

    fun update(statedRiver: StatedRiver) {
        gameMap.update(statedRiver)
    }
}