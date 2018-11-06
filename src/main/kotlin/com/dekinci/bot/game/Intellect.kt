package com.dekinci.bot.game

import com.dekinci.bot.game.tactics.ConnectMinesTactics
import com.dekinci.bot.game.tactics.PassTactics
import com.dekinci.bot.game.tactics.Tactics
import com.dekinci.bot.moves.Move

class Intellect(private val gameState: GameState) {
    private var minePosition = 0
    private var tactics: Tactics = PassTactics()

    init {
        changeTactics()
    }

    fun chooseMove(): Move {
        while (!tactics.hasNext())
            changeTactics()

        val move = tactics.next()
        println("Next move from ${tactics.javaClass.simpleName} is ${move.javaClass.simpleName}")

        return tactics.next()
    }

    private fun changeTactics() {
        when {
            minePosition < gameState.mines.size - 1 -> {
                tactics = ConnectMinesTactics(gameState, gameState.mines[minePosition], gameState.mines[minePosition + 1])
                minePosition++
            }
            minePosition == gameState.mines.size - 1 -> {
                tactics = ConnectMinesTactics(gameState, gameState.mines[minePosition], gameState.mines[0])
                minePosition++
            }
            else -> tactics = PassTactics()
        }

        println("Changing tactics to ${tactics.javaClass.simpleName}")
    }
}