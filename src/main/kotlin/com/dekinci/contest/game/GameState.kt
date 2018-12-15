package com.dekinci.contest.game

import com.dekinci.contest.entities.BasicMap
import com.dekinci.contest.entities.StatedRiver
import com.dekinci.contest.game.map.GameMap

class GameState(val punter: Int, val playersAmount: Int, basicMap: BasicMap) {
    val gameMap = GameMap(basicMap)

    fun update(statedRiver: StatedRiver) {
        gameMap.update(statedRiver)
    }
}