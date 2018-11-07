package com.dekinci.bot.game.tactics

import com.dekinci.bot.game.map.GameMap

import java.util.*

class PathFinder(private val map: GameMap) {
    private val parentMap = HashMap<Int, Int>()

    fun findPath(start: Int, finish: Int): List<Int> {
        println("Finding path from $start to $finish")

        val openList = PriorityQueue<Int>()
        val closedList = LinkedList<Int>()

        parentMap[start] = -1
        openList.add(start)

        while (!openList.isEmpty()) {
            val node = openList.remove()
            if (node == finish)
                return constructPath(finish)

            val neighbors = map.getAvailableConnections(node)
            for (neighbor in neighbors) {
                val isOpen = openList.contains(neighbor)
                val isClosed = closedList.contains(neighbor)

                if (!isOpen && !isClosed && map.realMetrics[start, node] < map.realMetrics[start, neighbor]) {
                    parentMap[neighbor] = node

                    if (isClosed)
                        closedList.remove(neighbor)

                    if (!isOpen)
                        openList.add(neighbor)
                }
            }

            closedList.add(node)
        }

        return emptyList()
    }

    private fun constructPath(node: Int): List<Int> {

        var varNode = node
        val path = LinkedList<Int>()
        while (parentMap[varNode] != -1) {
            path.addFirst(varNode)
            varNode = parentMap[varNode]!!
        }

        println("Path found: $path")

        return path
    }
}