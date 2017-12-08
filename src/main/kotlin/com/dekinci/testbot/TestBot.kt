package com.dekinci.testbot

import com.dekinci.connection.Connection
import ru.spbstu.competition.protocol.Protocol
import ru.spbstu.competition.protocol.data.GameResult

class TestBot(connection: Connection) : Runnable {
    val protocol = Protocol(connection.url, connection.port)

    override fun run() {
        initialize()
        playAGame()
        println("Dummy at ${Thread.currentThread().id} died")
    }

    private fun initialize() {
        protocol.handShake("Dummy")
        protocol.setup()
        protocol.ready()
        println("Added dummy on id ${protocol.myId} with thread ${Thread.currentThread().id}")
    }

    private fun playAGame() {
        var gameIsOn = true

        try {
            while (gameIsOn) {
                val message = protocol.serverMessage()
                when (message) {
                    is GameResult -> gameIsOn = false
                }
                println("Dummy passes")
                protocol.passMove()
            }
        }
        finally {
            println("Dummy at ${Thread.currentThread().id} died violently")
        }
    }
}
