package com.dekinci.contest.common

import com.dekinci.contest.entities.River
import com.dekinci.contest.entities.StatedRiver

fun riversToSiteSet(rivers: Iterable<River>): HashSet<Int> {
    val res = HashSet<Int>()
    rivers.forEach {
        res.add(it.source)
        res.add(it.target)
    }
    return res
}

fun statedRiversToSiteSet(rivers: Iterable<StatedRiver>): HashSet<Int> {
    val res = HashSet<Int>()
    rivers.forEach {
        res.add(it.source)
        res.add(it.target)
    }
    return res
}