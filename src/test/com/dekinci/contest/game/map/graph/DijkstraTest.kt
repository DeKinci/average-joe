import com.dekinci.contest.connectedMap
import com.dekinci.contest.disconnectedMap
import com.dekinci.contest.entities.River
import com.dekinci.contest.game.map.GameMap
import com.dekinci.contest.game.map.graph.AdjacencyList
import com.dekinci.contest.game.map.graph.Dijkstra
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class DijkstraTest {

    @Test
    fun test() {
        val d = dFromMap(connectedMap())

        assertEquals(intArrayOf(0, 1, 2, 1, 1, 3, 2, 2, 2).toList(), d.sparse(0).toList())
        assertEquals(intArrayOf(1, 1, 1, 2, 0, 2, 1, 1, 1).toList(), d.sparse(4).toList())
    }

    @Test
    fun testDiv() {
        val d = dFromMap(disconnectedMap())

        assertEquals(intArrayOf(-1, -1, -1, -1, -1, 0, -1, -1, 1).toList(), d.sparse(5).toList())
        assertEquals(intArrayOf(0, 1, 2, 1, 1, -1, 2, 2, -1).toList(), d.sparse(0).toList())
    }

    private fun dFromMap(map: GameMap): Dijkstra {
        val adjList = AdjacencyList(map.basicMap.size, map.basicMap.rivers)
        return Dijkstra(map.basicMap.size, adjList)
    }
}