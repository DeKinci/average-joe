package com.dekinci.bot.game.tactics

import com.dekinci.bot.game.map.GameMap

import java.util.*

class AStar(private val map: GameMap) {
    private class PriorityList : LinkedList<Int>() {
        override fun add(element: Int): Boolean {
            for (i in 0 until size)
                if (element <= get(i)) {
                    add(i, element)
                    return false
                }

            addLast(element)
            return true
        }
    }

    private val parentMap = HashMap<Int, Int>()

    private fun constructPath(node: Int): List<Int> {
        var varNode = node
        val path = LinkedList<Int>()
        while (parentMap[varNode] != -1) {
            path.addFirst(varNode)
            varNode = parentMap[varNode]!!
        }

        return path
    }

    fun findPath(start: Int, finish: Int): List<Int> {
        val openList = PriorityList()
        val closedList = LinkedList<Int>()

        parentMap[start] = -1
        openList.add(start)

        while (!openList.isEmpty()) {
            val node = openList.removeFirst()
            if (node == finish)
                return constructPath(finish)

            val neighbors = map.getFreeConnections(node!!)
            for (neighbor in neighbors) {
                val isOpen = openList.contains(neighbor)
                val isClosed = closedList.contains(neighbor)

                if (!isOpen && !isClosed || map.realMetrics[start, node] < map.realMetrics[start, neighbor]) {
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
}