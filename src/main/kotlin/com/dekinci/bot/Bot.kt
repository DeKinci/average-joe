package com.dekinci.bot

import com.dekinci.bot.game.Intellect
import com.dekinci.bot.game.State
import com.dekinci.bot.game.player.PlayersManager
import com.dekinci.connection.Connection
import ru.spbstu.competition.protocol.Protocol
import ru.spbstu.competition.protocol.data.*

open class Bot(private val name: String, connection: Connection) : Runnable {
    private var intellect: Intellect? = null
    private var gameState: State? = null
    private var manager: PlayersManager? = null

    private val protocol = Protocol(connection.url, connection.port)

    @Volatile
    var isPlaying = false

    override fun run() {
        initialize()
        playAGame()
    }

    private fun initialize() {
        println("Hi, I am $name, the ultimate punter!")

        protocol.handShake("$name, sup!")
        val setupData = protocol.setup()
        println("setup passed with " +
                setupData.map.sites.size + " nodes, " +
                setupData.map.rivers.size + " rivers and " +
                setupData.map.mines.size + " mines")

        gameState = State(setupData)
        manager = PlayersManager(gameState!!.gameMap, setupData.punters)

        println("state and manager initialized, working on intellect...")
        intellect = Intellect(gameState!!)

        println("Received id = ${setupData.punter}")

        protocol.ready()
        isPlaying = true
    }

    private fun playAGame() {
        while (isPlaying) {
            val message = protocol.serverMessage()

            when (message) {
                is GameResult -> {
                    println("The game is over!")
                    val myScore = message.stop.scores[protocol.myId]
                    println("$name scored ${myScore.score} points!")
                    isPlaying = false
                }

                is Timeout -> println("$name too slow =(")

                is GameTurnMessage ->
                    message.move.moves
                            .filterIsInstance<ClaimMove>()
                            .forEach { gameState?.update(it.claim) }
            }

            intellect!!.chooseMove().move(protocol)
        }
    }
}