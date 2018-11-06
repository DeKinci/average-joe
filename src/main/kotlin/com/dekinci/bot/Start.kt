package com.dekinci.bot

import com.dekinci.connection.Connection
import org.kohsuke.args4j.CmdLineParser
import org.kohsuke.args4j.Option

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

    while (!jack.isPlaying) {
        Thread(Bot(name, connection)).start()
        Thread.sleep(2000)
    }
}