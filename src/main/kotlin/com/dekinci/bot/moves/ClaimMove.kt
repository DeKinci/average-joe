package com.dekinci.bot.moves

import ru.spbstu.competition.protocol.Protocol

class ClaimMove(private var from: Int, private var to: Int) : Move {
    override fun move(protocol: Protocol) {
        protocol.claimMove(from, to)
    }

    override fun toString(): String {
        return "Claim ($from, $to)"
    }
}