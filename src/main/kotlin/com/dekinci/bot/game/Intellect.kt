package com.dekinci.bot.game

import com.dekinci.bot.entities.RiverState
import com.dekinci.bot.moves.ClaimMove
import com.dekinci.bot.moves.Move
import com.dekinci.bot.moves.PassMove

class Intellect(private val state: State) {
    fun chooseMove(): Move {

        // If there is a free river near a mine, take it!
        val try0 = state.rivers.find { river ->
            river.state == RiverState.Neutral && (river.source in state.mines || river.target in state.mines)
        }
        println(try0)
        if (try0 != null) return ClaimMove(try0.source, try0.target)

        // Look at all our pointsees
        val ourSites = state
                .rivers
                .findAll { river -> river.state == RiverState.Our }
                .flatMap { listOf(it.source, it.target) }
                .toSet()

        // If there is a river between two our pointsees, take it!
        val try1 = state.rivers.find { river ->
            river.state == RiverState.Neutral && (river.source in ourSites && river.target in ourSites)
        }
        if (try1 != null) return ClaimMove(try1.source, try1.target)

        // If there is a river near our pointsee, take it!
        val try2 = state.rivers.find { river ->
            river.state == RiverState.Neutral && (river.source in ourSites || river.target in ourSites)
        }
        if (try2 != null) return ClaimMove(try2.source, try2.target)

        // Bah, take anything left
        val try3 = state.rivers.find { river ->
            river.state == RiverState.Neutral
        }
        if (try3 != null) return ClaimMove(try3.source, try3.target)

        // (╯°□°)╯ ┻━┻
        return PassMove()
    }

}
