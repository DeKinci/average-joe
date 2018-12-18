package com.dekinci.contest.game.map.metric

class PotentialMetrics(
        sitesAmount: Int,
        private val distanceMetrics: DistanceMetrics
) {
    private val weights = IntArray(sitesAmount) { distanceMetrics.siteCost(it) }

    fun update(site: Int) {
        weights[site] = distanceMetrics.siteCost(site)
    }

    operator fun get(site: Int) = weights[site]
}