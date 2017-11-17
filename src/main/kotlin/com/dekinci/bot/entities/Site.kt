package com.dekinci.bot.entities

import com.dekinci.bot.utility.IDManager

data class Site(val id: Int, val x: Double?, val y: Double?) {
    init {
        IDManager.registerID(id)
    }
}