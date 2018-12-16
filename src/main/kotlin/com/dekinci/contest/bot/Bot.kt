package com.dekinci.contest.bot

import com.dekinci.contest.entities.StatedRiver

interface Bot {
    val name: String

    fun onTimeout() {
        println("$name too slow =(")
    }

    fun onUpdate(statedRiver: StatedRiver)

    fun getMove(): StatedRiver?

    fun onFinish()
}