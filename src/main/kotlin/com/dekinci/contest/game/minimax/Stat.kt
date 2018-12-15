package com.dekinci.contest.game.minimax

import java.util.concurrent.ConcurrentHashMap

object Stat {
    private val timeMap = ConcurrentHashMap<String, Long>()

    fun start(tag: String) {
        timeMap.compute(tag) { _, v -> (v ?: 0) - System.currentTimeMillis() }
    }

    fun end(tag: String) {
        timeMap.computeIfPresent(tag) { _, v -> v + System.currentTimeMillis() }
    }

    override fun toString(): String {
        return timeMap.entries.joinToString("\n") { "${it.key}: ${it.value}" }
    }
}