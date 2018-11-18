package com.dekinci.bot.entities

object IDManager {
    var maxID = -1

    fun registerID(id: Int) {
        if (id > maxID)
            maxID = id
    }
}