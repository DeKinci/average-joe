package com.dekinci.bot

import com.dekinci.connection.Connection
import com.dekinci.testbot.TestBot
import org.kohsuke.args4j.CmdLineParser
import org.kohsuke.args4j.Option
import java.net.ConnectException

object Arguments {
    @Option(name = "-u", usage = "Specify server url")
    var url: String = ""

    @Option(name = "-p", usage = "Specify server port")
    var port: Int = -1

    fun use(args: Array<String>): Arguments =
            CmdLineParser(this).parseArgument(*args).let { this }
}

fun main(args: Array<String>) {
    Arguments.use(args)

    val name = "Budding Jack"
    val connection = Connection(Arguments.url, Arguments.port)

    val jack = Bot(name, connection)

    Thread(jack).start()
    dummyfier2000(jack, connection)
}

fun dummyfier2000(bot: Bot, connection: Connection) {
    Thread {
        while (true) {
            Thread.sleep(2000)
            if (bot.isPlaying)
                break
            addDummy(connection)
        }
    }.start()
}

fun addDummy(connection: Connection) {
    try {
        Thread(TestBot(connection)).also {
            it.isDaemon = true
            it.start()
        }
    } catch (e: ConnectException) {
        System.err.println("Dummy connection error")
    }
}