package com.dekinci.bot.game.map.graphstuff

class MatrixAdapter (val size: Int) {
    private val matrix = JavaMatrix(size)

    operator fun get(x: Int, y: Int): Int =  matrix.get(x, y)

    operator fun set(x: Int, y: Int, t: Int) {
        matrix.set(x, y, t)
    }

    fun forEachHorizontal(row: Int, operation: (Int, column: Int) -> Unit) {
        //TODO
    }

    fun forEachIndexed(operation: (x: Int, y: Int, Int) -> Unit) {
        //TODO
    }
}
