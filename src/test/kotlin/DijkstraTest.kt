import com.dekinci.bot.game.map.graphstuff.Dijkstra

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.ArrayList

class DijkstraTest {
    private val INF = Integer.MAX_VALUE / 2

    @Test
    fun test() {
        assertEquals(intArrayOf(0, 1, 1, 2, 2, INF, INF).toList(), dijkstra().toList())
    }

    private fun dijkstra(): IntArray {
        val vertexAmount = 7
        val start = 0

        val adjacencyList = ArrayList<ArrayList<Int>>(vertexAmount)
        for (i in 0 until vertexAmount)
            adjacencyList.add(ArrayList())

        val data = "6 7 2 4 1 2 1 3 2 5 3 2 3 5 4 5 5 3 5 4 7 6 1 3"
                .split(" ")
                .map { Integer.parseInt(it) }

        val edgeAmount = 11

        var i = 0
        while (i < edgeAmount) {
            adjacencyList[data[i] - 1].add(data[i + 1] - 1)
            i += 2
        }

        Dijkstra.init(vertexAmount, adjacencyList)

        val dijkstra = Dijkstra()
        return dijkstra.sparse(start)
    }
}