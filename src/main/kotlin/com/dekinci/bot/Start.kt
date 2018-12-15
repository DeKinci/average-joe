package com.dekinci.bot

import com.dekinci.bot.entities.BasicMap
import com.dekinci.bot.game.minimax.Stat
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

    val connection = ServerConnection(Arguments.url, Arguments.port)

    val br = BotRunner()

    Runtime.getRuntime().addShutdownHook(Thread { println(Stat.toString()) })
    br.runBot(connection, MinimaxFactory()).get()
    br.shutdown()
}

class MinimaxFactory : BotFactory {
    override fun getBotName() = "Vladimir Minimax"

    override fun makeBot(punter: Int, punters: Int, map: BasicMap) = BotImpl(getBotName(), punter, punters, map)
}