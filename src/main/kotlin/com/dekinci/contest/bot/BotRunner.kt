package com.dekinci.contest.bot

import com.dekinci.contest.common.Log.debug
import com.dekinci.contest.protocol.ServerConnection
import java.util.concurrent.Executors
import java.util.concurrent.Future

class BotRunner {
    private val executor = Executors.newSingleThreadExecutor()

    fun runBot(connection: ServerConnection, botFactory: BotFactory): Future<*> {
        debug("Starting executor")
        return executor.submit { BotMaster(connection, botFactory).play() }
    }

    fun shutdown() {
        debug("Shutting down")
        executor.shutdown()
    }
}