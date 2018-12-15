package com.dekinci.bot.game.map.graph

import java.util.*

class Dijkstra(private val vertexAmount: Int, private val adjacencyList: AdjacencyList) {
    private val INF = Integer.MAX_VALUE / 2

    fun sparse(start: Int): IntArray {
        val queue = LinkedList<Int>()
        val dist = IntArray(vertexAmount) { INF }

        dist[start] = 0
        queue.add(start)

        while (!queue.isEmpty()) {
            val from = queue.first()
            queue.remove(from)

            for (to in adjacencyList[from])
                if (dist[from] + 1 < dist[to]) {
                    queue.remove(to)
                    queue.addLast(to)
                    dist[to] = dist[from] + 1
                }
        }

        for (i in 0 until dist.size)
            if (dist[i] == INF)
                dist[i] = -1

        return dist
    }
}