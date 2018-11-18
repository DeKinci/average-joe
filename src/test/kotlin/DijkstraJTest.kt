import com.dekinci.bot.entities.StatedRiver
import com.dekinci.bot.game.map.graphstuff.AdjacencyList
import com.dekinci.bot.game.map.graphstuff.Dijkstra

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class DijkstraJTest {
    private val INF = Integer.MAX_VALUE / 2

    @Test
    fun test() {
        assertEquals(intArrayOf(0, 1, 1, 2, 2, INF, INF).toList(), dijkstra().toList())
    }

    private fun dijkstra(): IntArray {
        val vertexAmount = 7
        val start = 0

        val riverList = ArrayList<StatedRiver>()

        val data = "6 7 2 4 1 2 1 3 2 5 3 2 3 5 4 5 5 3 5 4 7 6 1 3"
                .split(" ")
                .map { Integer.parseInt(it) - 1 }

        for (i in 0 until data.size step 2)
            riverList.add(StatedRiver(data[i], data[i + 1]))

        val dijkstra = Dijkstra(vertexAmount, AdjacencyList(vertexAmount, riverList))
        return dijkstra.sparse(start)
    }

    @Test
    fun testDiv() {

        println(Arrays.toString(dijkstraDivided(5)))
    }

    /**
     *  0---1---2
     *  | \ | /
     *  3   4   5
     *  | / |   |
     *  6---7   8
     */
    private fun dijkstraDivided(start: Int): IntArray {
        val vertexAmount = 9

        val riverList = ArrayList<StatedRiver>()

        val data = "0 1 1 2 0 3 0 4 1 4 2 4 3 6 4 7 5 8 6 7"
                .split(" ")
                .map { Integer.parseInt(it) }

        for (i in 0 until data.size step 2)
            riverList.add(StatedRiver(data[i], data[i + 1]))

        val dijkstra = Dijkstra(vertexAmount, AdjacencyList(vertexAmount, riverList))
        return dijkstra.sparse(start)
    }

}