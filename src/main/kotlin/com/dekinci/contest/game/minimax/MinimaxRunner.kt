package com.dekinci.contest.game.minimax

import com.dekinci.contest.entities.StatedRiver
import java.util.concurrent.Executors
import java.util.concurrent.Future

class MinimaxRunner(val minimax: Minimax) {
    private val executor = Executors.newSingleThreadExecutor()

    private var task: Future<*>? = null

    fun update(river: StatedRiver) {
        minimax.update(river)
    }
}