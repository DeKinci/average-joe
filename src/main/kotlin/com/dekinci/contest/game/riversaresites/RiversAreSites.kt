package com.dekinci.contest.game.riversaresites

import com.dekinci.contest.entities.River
import com.dekinci.contest.game.map.graph.AdjacencyList

class RiversAreSites(
        private val sitesAmount: Int,
        private val totalList: AdjacencyList,
        private val rivers: Array<River>,
        private val mines: Set<Int>
) {
    private val adjList: Array<HashSet<Int>> = Array(rivers.size) { HashSet<Int>() }


}