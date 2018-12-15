package com.dekinci.bot

import com.dekinci.bot.entities.Rectifier
import com.dekinci.bot.entities.StatedRiver
import ru.spbstu.competition.protocol.Protocol
import ru.spbstu.competition.protocol.ServerConnection
import ru.spbstu.competition.protocol.data.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class BotMaster(connection: ServerConnection, botFactory: BotFactory) {
    private val protocol = Protocol(connection)
    private val rectifier: Rectifier

    private val playingFlag = AtomicBoolean()

    private val passStreak = AtomicInteger()

    private val bot: Bot

    init {
        println("Hi, I am ${botFactory.getBotName()}, the ultimate punter!")
        protocol.handShake(botFactory.getBotName())

        val setupData = protocol.setup()
        println("""setup passed with
            ${setupData.map.sites.size} nodes,
            ${setupData.map.rivers.size} rivers and
            ${setupData.map.mines.size} mines""".trimMargin())

        rectifier = Rectifier(setupData.map.rivers, setupData.map.mines)

        bot = botFactory.makeBot(setupData.punter, setupData.punters, rectifier.asMap())
        protocol.ready()
        playingFlag.set(true)
        println("Received id = ${setupData.punter}")
    }

    fun play() {
        println("${bot.name} started")
        gameCycle@ while (playingFlag.get()) {
            val message = protocol.serverMessage()
            handleMessage(message)

            if (!playingFlag.get())
                break@gameCycle

            makeAMove()
        }
        println("${bot.name} finished")
    }

    private fun makeAMove() {
        val move = bot.getMove()
        if (move == null)
            handlePass()
        else {
            rectifier.pollute(move)
            protocol.claimMove(move.target, move.source)
        }
    }

    private fun handleMessage(message: ServerMessage) {
        when (message) {
            is GameTurnMessage -> handleTurn(message)
            is Timeout -> handleTimeout()
            is GameResult -> handleResult(message)
        }
    }

    private fun handleTurn(move: GameTurnMessage) {
        passStreak.set(0)
        move.move.moves
                .filterIsInstance<ClaimMove>()
                .map { rectifier.purify(StatedRiver(it.claim)) }
                .forEach { bot.onUpdate(it) }
    }

    private fun handleTimeout() {
        handlePass()
        bot.onTimeout()
    }

    private fun handlePass() {
        if (passStreak.incrementAndGet() > 11)
            playingFlag.set(false)
    }

    private fun handleResult(result: GameResult) {
        val myScore = result.stop.scores[protocol.myId]
        println("The game is over!")
        println("${bot.name} scored ${myScore.score} points!")
        playingFlag.set(false)
    }
}