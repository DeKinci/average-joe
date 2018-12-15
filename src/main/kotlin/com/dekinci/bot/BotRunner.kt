package com.dekinci.bot

import ru.spbstu.competition.protocol.ServerConnection
import java.util.concurrent.Executors
import java.util.concurrent.Future

class BotRunner {
    private val executor = Executors.newSingleThreadExecutor()

    fun runBot(connection: ServerConnection, botFactory: BotFactory): Future<*> {
        return executor.submit { BotMaster(connection, botFactory).play() }
    }

    fun shutdown() {
        executor.shutdown()
    }
}