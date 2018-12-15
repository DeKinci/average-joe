package com.dekinci.contest.protocol

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket


class ServerConnection(url: String, port: Int) {

    private var socket = Socket(url, port)

    var sin = BufferedReader(InputStreamReader(socket.getInputStream()))
    var sout = PrintWriter(socket.getOutputStream(), true)

    init {
        socket.setSoLinger(true, 10)
    }

    fun <T> sendJson(json: T) {
        val jsonString = objectMapper.writeValueAsString(json)

        sout.println("${jsonString.length}:${jsonString}")
        sout.flush()
    }

    inline fun <reified T> receiveJson(): T {
        val lengthChars = mutableListOf<Char>()
        var ch = '0'
        while (ch != ':') {
            lengthChars += ch
            ch = sin.read().toChar()
        }

        val length = lengthChars.joinToString("").trim().toInt()
        // Чтение из Reader нужно делать очень аккуратно
        val contentAsArray = CharArray(length)
        var start = 0
        // Операция read не гарантирует нам, что вернулось именно нужное количество символов
        // Поэтому её нужно делать в цикле
        while (start < length) {
            val read = sin.read(contentAsArray, start, length - start)
            start += read
        }

        return objectMapper.readValue(String(contentAsArray), T::class.java)
    }

}
