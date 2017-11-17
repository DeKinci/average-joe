package com.dekinci.bot.moves

import ru.spbstu.competition.protocol.Protocol

interface Move {
    fun move(protocol: Protocol)
}