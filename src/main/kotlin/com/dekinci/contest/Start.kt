package com.dekinci.contest

import com.dekinci.contest.bot.BotFactory
import com.dekinci.contest.bot.BotImpl
import com.dekinci.contest.bot.BotRunner
import com.dekinci.contest.bot.MathBot
import com.dekinci.contest.common.Log.info
import com.dekinci.contest.entities.BasicMap
import com.dekinci.contest.game.minimax.Stat
import org.kohsuke.args4j.CmdLineParser
import org.kohsuke.args4j.Option
import com.dekinci.contest.protocol.ServerConnection

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
    info("Starting on ${Arguments.url} : ${Arguments.port}")

    val connection = ServerConnection(Arguments.url, Arguments.port)

    val br = BotRunner()

    Runtime.getRuntime().addShutdownHook(Thread { info(Stat.toString()) })
    br.runBot(connection, MatBoiFactory()).get()
    br.shutdown()
}

class MinimaxFactory : BotFactory {
    override fun getBotName() = "Test"

    override fun makeBot(punter: Int, punters: Int, map: BasicMap) = BotImpl(getBotName(), punter, punters, map)
}

class MatBoiFactory : BotFactory {
    override fun getBotName() = "Math boi"

    override fun makeBot(punter: Int, punters: Int, map: BasicMap) = MathBot(getBotName(), punter, punters, map)
}