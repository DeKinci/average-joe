package com.dekinci.bot.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
interface Site {
    fun getID(): Int
}