package com.dekinci.contest.bot

import com.dekinci.contest.common.Log.debug
import com.dekinci.contest.common.Log.err
import com.dekinci.contest.common.Log.info
import com.dekinci.contest.common.Log.trace
import com.dekinci.contest.common.Log.warn
import com.dekinci.contest.entities.Rectifier
import com.dekinci.contest.entities.StatedRiver
import com.dekinci.contest.protocol.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class BotMaster(connection: ServerConnection, botFactory: BotFactory) {
    private val protocol = Protocol(connection)
    private val rectifier: Rectifier

    private val playingFlag = AtomicBoolean()

    private val passStreak = AtomicInteger()

    private val bot: Bot

    init {
        info("Hi, I am ${botFactory.getBotName()}, the ultimate punter!")
        protocol.handShake(botFactory.getBotName())

        val setupData = protocol.setup()
        info(("setup passed with " +
                "${setupData.map.sites.size} nodes, " +
                "${setupData.map.rivers.size} rivers and " +
                "${setupData.map.mines.size} mines").trimMargin())

        rectifier = Rectifier(setupData.map.rivers, setupData.map.mines)

        bot = botFactory.makeBot(setupData.punter, setupData.punters, rectifier.asMap())
        protocol.ready()
        playingFlag.set(true)
        info("Received id = ${setupData.punter}")
    }

    fun play() {
        debug("${bot.name} started")
        gameCycle@ while (playingFlag.get()) {
            trace("Game iteration")
            val message = protocol.serverMessage()
            handleMessage(message)

            if (!playingFlag.get())
                break@gameCycle

            makeAMove()
        }
        bot.onFinish()
        debug("${bot.name} finished")
    }

    private fun makeAMove() {
        val move = bot.getMove()
        debug("Move: $move")
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
            else -> warn("Strange message: $message")
        }
    }

    private fun handleTurn(move: GameTurnMessage) {
        trace("Turn: ${move.move}")
        passStreak.set(0)
        move.move.moves
                .filterIsInstance<ClaimMove>()
                .map { rectifier.purify(StatedRiver(it.claim)) }
                .forEach { bot.onUpdate(it) }
    }

    private fun handleTimeout() {
        err("Timeout")
        handlePass()
        bot.onTimeout()
    }

    private fun handlePass() {
        warn("Pass move")
        if (passStreak.incrementAndGet() > 11)
            playingFlag.set(false)
    }

    private fun handleResult(result: GameResult) {
        val myScore = result.stop.scores[protocol.myId]
        info("The game is over!")
        info("${bot.name} scored ${myScore.score} points!")
        playingFlag.set(false)
    }
}