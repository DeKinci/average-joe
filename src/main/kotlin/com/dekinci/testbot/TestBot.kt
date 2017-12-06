package com.dekinci.testbot

import com.dekinci.connection.Connection
import ru.spbstu.competition.protocol.Protocol
import ru.spbstu.competition.protocol.data.GameResult

class TestBot(connection: Connection) : Runnable {
    val protocol = Protocol(connection.url, connection.port)

    override fun run() {
        initialize()
        playAGame()
    }

    private fun initialize() {
        protocol.handShake("Dummy")
        protocol.ready()
    }

    private fun playAGame() {
        var gameIsOn = true
        while (gameIsOn) {
            val message = protocol.serverMessage()
            when (message) {
                is GameResult -> gameIsOn = false
            }
            protocol.passMove()
        }
    }
}
