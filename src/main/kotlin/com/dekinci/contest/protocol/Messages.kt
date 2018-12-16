package com.dekinci.contest.protocol

import com.dekinci.contest.entities.River
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.JsonNode

data class HandshakeRequest(val me: String)
data class HandshakeResponse(val you: String)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Site(val id: Int)
data class Map(val sites: List<Site>, val rivers: List<River>, val mines: List<Int>)
data class Setup(val punter: Int, val punters: Int, val map: Map, val settings: JsonNode?)

data class Ready(val ready: Int)

data class Pass(val punter: Int)
data class Claim(val punter: Int, val source: Int, val target: Int)

data class PassMove(val pass: Pass): Move()
data class ClaimMove(val claim: Claim): Move()

sealed class Move {
    companion object {
        @JvmStatic
        @JsonCreator
        fun factory(map: kotlin.collections.Map<String, Any>): Move {
            return when {
                "pass" in map -> objectMapper.convertValue(map, PassMove::class.java)
                "claim" in map -> objectMapper.convertValue(map, ClaimMove::class.java)
                else -> throw IllegalArgumentException()
            }
        }
    }
}

data class GameTurn(val moves: List<Move>)
data class GameTurnMessage(val move: GameTurn): ServerMessage()

data class Score(val punter: Int, val score: Int)
data class GameStop(val moves: List<Move>, val scores: List<Score>)

data class GameResult(val stop: GameStop): ServerMessage()
data class Timeout(val timeout: Double): ServerMessage()

@JsonIgnoreProperties(ignoreUnknown = true)
class Staff: ServerMessage()

sealed class ServerMessage {
    companion object {
        @JvmStatic
        @Synchronized
        @JsonCreator
        fun factory(map: kotlin.collections.Map<String, Any>): ServerMessage {
            return when {
                "move" in map -> objectMapper.convertValue(map, GameTurnMessage::class.java)
                "stop" in map -> objectMapper.convertValue(map, GameResult::class.java)
                "timeout" in map -> objectMapper.convertValue(map, Timeout::class.java)
                "map" in map -> objectMapper.convertValue(map, Staff::class.java)
                else -> throw IllegalArgumentException()
            }
        }
    }
}
