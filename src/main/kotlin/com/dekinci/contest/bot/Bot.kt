package com.dekinci.contest.bot

import com.dekinci.contest.common.Log.warn
import com.dekinci.contest.entities.StatedRiver

interface Bot {
    val name: String

    fun onTimeout() {}

    fun onUpdate(statedRiver: StatedRiver)

    fun getMove(): StatedRiver?

    fun onFinish() {}
}