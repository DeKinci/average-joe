package ru.spbstu.competition.protocol

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.lang.IllegalStateException
import java.net.Socket
import java.net.SocketException
import java.util.concurrent.locks.ReentrantLock


class ServerConnection(private val url: String, private val port: Int) {

    // Для связи с сервером для начала нужно создать сокет
    private var socket = Socket(url, port)
//    private val streamLock = ReentrantLock()

    // Потом из сокета можно получить потоки ввода и вывода, и работать с ним, как с файлом
    // В нашей задаче можно сделать BufferedReader/PrintWriter, потому что протокол текстовый
    var sin = BufferedReader(InputStreamReader(socket.getInputStream()))
    var sout = PrintWriter(socket.getOutputStream(), true)

    init {
        socket.soTimeout = 0
    }

    // Отправка сообщения на сервер
    fun <T> sendJson(json: T) {
        val jsonString = objectMapper.writeValueAsString(json)

        // См. описание протокола
        sout.println("${jsonString.length}:${jsonString}")
        // Буферизованные потоки в Java требуют вызова метода flush(), чтобы
        // записанное точно отправилось на сервер
        sout.flush()
    }

    // Получение сообщения с сервера
    // Здесь используется продвинутая фича Котлина - reified generics
    // Она требует, чтобы функция была inline и позволяет получить для T объект класса
    // (см. последнюю строку функции)
    inline fun <reified T> receiveJson(): T {
        val lengthChars = mutableListOf<Char>()
        var ch = '0'
        while (ch != ':') {
            lengthChars += ch
            ch = readSafely().toChar()
        }

        val length = lengthChars.joinToString("").trim().toInt()
        // Чтение из Reader нужно делать очень аккуратно
        val contentAsArray = CharArray(length)
        var start = 0
        // Операция read не гарантирует нам, что вернулось именно нужное количество символов
        // Поэтому её нужно делать в цикле
        while (start < length) {
            val read = readSafely(contentAsArray, start, length - start)
            start += read
        }

        //println("Json received")
        return objectMapper.readValue(String(contentAsArray), T::class.java)
    }

    fun readSafely(attempts: Int = 5): Int {
        if (attempts == 0)
            throw IllegalStateException("attempts out")

        return try {
            sin.read()
        } catch (e: SocketException) {
            handleSocketException(e)
            readSafely(attempts - 1)
        }
    }

    fun readSafely(cbuf: CharArray, off: Int, len: Int, attempts: Int = 5): Int {
        if (attempts == 0)
            throw IllegalStateException("attempts out")

        return try {
            sin.read(cbuf, off, len)
        } catch (e: SocketException) {
            handleSocketException(e)
            readSafely(cbuf, off, len, attempts - 1)
        }
    }

    private fun handleSocketException(e: SocketException) {
//        streamLock.lock()
        try {
            e.printStackTrace()
            sin.close()
            sout.close()
            socket.close()
            socket = Socket(url, port)
            sin = BufferedReader(InputStreamReader(socket.getInputStream()))
            sout = PrintWriter(socket.getOutputStream(), true)
        } finally {
//            streamLock.unlock()
        }
    }

}
