package com.dekinci.bot

import ru.spbstu.competition.protocol.Protocol

class Intellect(val state: State, val protocol: Protocol) {

    fun makeMove() {
        // Joe is like super smart!
        // Da best strategy ever!

        // If there is a free river near a mine, take it!
        val try0 = state.rivers.find { river ->
            river.state == RiverState.Neutral && (river.source in state.mines || river.target in state.mines)
        }
        if(try0 != null) return protocol.claimMove(try0.source, try0.target)

        // Look at all our pointsees
        val ourSites = state
                .rivers
                .findAll { river ->  river.state == RiverState.Our}
                .flatMap { listOf(it.source, it.target) }
                .toSet()

        // If there is a river between two our pointsees, take it!
        val try1 = state.rivers.find { river ->
            river.state == RiverState.Neutral && (river.source in ourSites && river.target in ourSites)
        }
        if(try1 != null) return protocol.claimMove(try1.source, try1.target)

        // If there is a river near our pointsee, take it!
        val try2 = state.rivers.find { river ->
            river.state == RiverState.Neutral && (river.source in ourSites || river.target in ourSites)
        }
        if(try2 != null) return protocol.claimMove(try2.source, try2.target)

        // Bah, take anything left
        val try3 = state.rivers.find { river ->
            river.state == RiverState.Neutral
        }
        if (try3 != null) return protocol.claimMove(try3.source, try3.target)

        // (╯°□°)╯ ┻━┻
        protocol.passMove()
    }

}
