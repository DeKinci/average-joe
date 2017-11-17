package com.dekinci.bot.utility

class IDManager {
    companion object {
        var maxID = -1

        fun registerID(id: Int) {
            if (id > maxID)
                maxID = id
        }
    }
}