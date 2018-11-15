package com.dekinci.bot

import com.dekinci.bot.game.GameState
import com.dekinci.bot.game.Intellect
import ru.spbstu.competition.protocol.Protocol
import ru.spbstu.competition.protocol.ServerConnection
import ru.spbstu.competition.protocol.data.*

class Bot(private val name: String, connection: ServerConnection) : Runnable {
    private var intellect: Intellect? = null
    private var gameState: GameState? = null

    private val protocol = Protocol(connection)

    @Volatile
    var isPlaying = false

    override fun run() {
        initialize()
        playAGame()
        println("$name finished")
    }

    private fun initialize() {
        println("Hi, I am $name, the ultimate punter!")

        protocol.handShake("$name, sup!")
        val setupData = protocol.setup()
        println("""setup passed with
            ${setupData.map.sites.size} nodes,
            ${setupData.map.rivers.size} rivers and
            ${setupData.map.mines.size} mines""".trimMargin())

        isPlaying = true

        gameState = GameState(setupData)
        intellect = Intellect(gameState!!)

        println("Received id = ${setupData.punter}")

        protocol.ready()
    }

    private fun playAGame() {
        var passCounter = 0

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
                            .forEach { claimRiver(it.claim) }
            }

            val move = intellect!!.chooseMove()

            if (move is com.dekinci.bot.moves.PassMove)
                passCounter++
            else
                passCounter = 0

            if (passCounter > 15)
                System.exit(100)

            move.move(protocol)
        }
    }

    private fun claimRiver(claim: Claim) {
        gameState?.update(claim)
    }
}