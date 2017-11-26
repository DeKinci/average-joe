package com.dekinci.bot

import com.dekinci.bot.game.Intellect
import com.dekinci.bot.game.State
import ru.spbstu.competition.protocol.Protocol
import ru.spbstu.competition.protocol.data.*

class Bot(private val name: String, val protocol: Protocol) : Runnable {
    private var intellect: Intellect? = null
    private var gameState: State? = null

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

        println("state initialized, working on intellect...")
        intellect = Intellect(gameState!!)

        println("Received id = ${setupData.punter}")

        protocol.ready()
    }

    private fun playAGame() {
        var isPlaying = true
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