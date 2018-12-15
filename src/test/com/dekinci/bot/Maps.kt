package com.dekinci.bot

import com.dekinci.bot.entities.BasicMap
import com.dekinci.bot.entities.Rectifier
import com.dekinci.bot.entities.River
import com.dekinci.bot.game.map.GameMap

/**
 *  0---1---2!
 *  | \ | / |
 * !3   4   5
 *  | / | \ |
 *  6---7---8
 */
fun connectedMap(): GameMap {
    val size = 9
    val mines = setOf(3, 2)
    val rivers = riversFromString("0,1 1,2 0,3 0,4 1,4 4,2 2,5 3,6 6,4 7,4 4,8 8,5 6,7 7,8")

    return gameMapFrom(rivers, mines, size)
}

/**
 *  0---1---2!
 *  | \ | /
 * !3   4   5!
 *  | / |   |
 *  6---7   8
 */
fun disconnectedMap(): GameMap {
    val size = 9
    val mines = setOf(3, 2, 5)
    val rivers = riversFromString("0,1 1,2 0,3 0,4 1,4 2,4 3,6 4,7 4,6 5,8 6,7")

    return gameMapFrom(rivers, mines, size)
}

/**
 * !0---1---2!
 *      |
 *      3
 */
fun microMap(): GameMap {
    val size = 4
    val mines = setOf(0, 2)
    val rivers = riversFromString("0,1 1,2 1,3")
    return gameMapFrom(rivers, mines, size)
}

/**
 * !0---1
 */
fun nanoMap() = GameMap(BasicMap(arrayOf(River(0, 1)), setOf(0), 2))

fun fromDirtyMap(rivers: Iterable<River>, mines: Iterable<Int>): GameMap {
    val rectifier = Rectifier(rivers, mines)
    return GameMap(rectifier.asMap())
}

fun gameMapFrom(rivers: Iterable<River>, mines: Iterable<Int>, size: Int) =
        GameMap(BasicMap(rivers.toList().toTypedArray(), mines.toHashSet(), size))

private fun riversFromString(s: String) = s.split(" ")
        .map { River(it.split(",")[0].toInt(), it.split(",")[1].toInt()) }