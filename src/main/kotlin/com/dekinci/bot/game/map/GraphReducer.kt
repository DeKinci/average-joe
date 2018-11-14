package com.dekinci.bot.game.map

import com.dekinci.bot.game.map.graphstuff.AdjacencyList
import com.dekinci.bot.game.map.graphstuff.AdjacencyMatrix

class GraphReducer(matrix: AdjacencyMatrix, list: AdjacencyList) {
    data class NodeInfo(val price: Int, val size: Int)

    private val nodeInfo = HashMap<Int, NodeInfo>()
    private val reducedList = ArrayList<HashSet<Int>>()


}