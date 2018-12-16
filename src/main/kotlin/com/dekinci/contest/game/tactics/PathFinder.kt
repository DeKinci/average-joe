package com.dekinci.contest.game.tactics

import com.dekinci.contest.game.map.GameMap

import java.util.*
import kotlin.collections.HashSet

class PathFinder(private val map: GameMap) {
    private data class TwoWayPath(val a: Int, val b: Int) {
        override fun equals(other: Any?): Boolean {
            if (this === other)
                return true
            if (other !is TwoWayPath)
                return false
            return other.a == b && other.b == a || other.a == a && other.b == b
        }

        override fun hashCode() = a.hashCode() xor b.hashCode()
    }

    private val parentMap = HashMap<Int, Int>()

    fun findPath(start: Int, finish: Int, punter: Int, excludePaths: List<List<Int>> = emptyList()): List<Int> {
        println("Finding path from $start to $finish")

        val openList = PriorityQueue<Int>()
        val closedList = LinkedList<Int>()

        val excludedTwoWayPaths = HashSet<TwoWayPath>()
        for (path in excludePaths) {
            if (path.isNotEmpty())
                for (i in 1 until path.size)
                    excludedTwoWayPaths.add(TwoWayPath(path[i - 1], path[i]))
        }

        parentMap[start] = -1
        openList.add(start)

        while (!openList.isEmpty()) {
            val node = openList.remove()
            if (node == finish)
                return constructPath(finish)

            val neighbors = map.getAvailableConnections(node, punter)

            for (neighbor in neighbors) {
                val isOpen = openList.contains(neighbor)
                val isClosed = closedList.contains(neighbor)

                if (!isOpen && !isClosed &&
                        map.squareMetrics[start, node] < map.squareMetrics[start, neighbor] &&
                        !excludedTwoWayPaths.contains(TwoWayPath(node, neighbor))) {
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