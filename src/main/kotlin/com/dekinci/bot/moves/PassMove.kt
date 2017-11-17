package com.dekinci.bot.moves

import ru.spbstu.competition.protocol.Protocol

class PassMove: Move {
    override fun move(protocol: Protocol) {
        protocol.passMove()
    }
}