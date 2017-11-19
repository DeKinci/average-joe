package com.dekinci.bot.game.map

class Matrix<T>(val array: Array<Array<T>>, val size: Int = array.size) {

    companion object {
        inline operator fun <reified T> invoke(size: Int, operator: (Int, Int) -> (T)): Matrix<T> {
            val array = Array(size, {
                Array(size, { operator(it, it) })
            })
            return Matrix(array)
        }
    }

    operator fun get(x: Int, y: Int): T = array[x][y]

    operator fun set(x: Int, y: Int, t: T) {
        array[x][y] = t
    }

    inline fun forEach(operation: (T) -> Unit) = array.forEach { it.forEach { operation.invoke(it) } }

    inline fun forEachIndexed(operation: (x: Int, y: Int, T) -> Unit) =
            array.forEachIndexed { x, p -> p.forEachIndexed { y, t -> operation.invoke(x, y, t) } }
}
