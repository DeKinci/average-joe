package com.dekinci.bot.game.minimax

import com.dekinci.bot.entities.StatedRiver
import java.util.concurrent.Executors
import java.util.concurrent.Future
import kotlin.math.min

class MinimaxRunner(val minimax: Minimax) {
    private val executor = Executors.newSingleThreadExecutor()

    private var task: Future<*>? = null

    fun update(river: StatedRiver) {
        minimax.update(river)
    }
}