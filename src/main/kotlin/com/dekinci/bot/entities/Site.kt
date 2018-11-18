package com.dekinci.bot.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Site(private val id: Int) {
    fun getID(): Int = id

    init {
        IDManager.registerID(id)
    }
}