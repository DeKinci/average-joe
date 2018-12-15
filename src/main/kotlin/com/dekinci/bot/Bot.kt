package com.dekinci.bot

import com.dekinci.bot.entities.StatedRiver

interface Bot {
    val name: String

    fun onTimeout() {
        println("$name too slow =(")
    }

    fun onUpdate(statedRiver: StatedRiver)

    fun getMove(): StatedRiver?
}