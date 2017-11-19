package com.dekinci.bot.entities

import com.dekinci.bot.utility.IDManager

class CommonSite(private val id: Int): Site {
    override fun getID(): Int = id

    init {
        IDManager.registerID(id)
    }
}