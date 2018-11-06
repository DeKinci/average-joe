package com.dekinci.bot

import org.kohsuke.args4j.CmdLineParser
import org.kohsuke.args4j.Option
import ru.spbstu.competition.protocol.ServerConnection

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
    val connection = ServerConnection(Arguments.url, Arguments.port)

    val jack = Bot(name, connection)
    Thread(jack).start()

//    var counter = 1
//    while (!jack.isPlaying) {
//        Thread.sleep(5000)
//        Thread(Bot("$name #$counter", ServerConnection(Arguments.url, Arguments.port))).start()
//        counter++
//    }
}