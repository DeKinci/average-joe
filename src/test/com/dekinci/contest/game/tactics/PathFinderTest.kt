import com.dekinci.contest.connectedMap
import com.dekinci.contest.disconnectedMap
import com.dekinci.contest.entities.River
import com.dekinci.contest.fromDirtyMap
import com.dekinci.contest.game.tactics.PathFinder
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.collections.ArrayList

class PathFinderTest {

    @Test
    fun test() {
        val map = connectedMap()

        val start = 3
        val path = listOf(start) + PathFinder(map).findPath(start, 2, 0, listOf(listOf(3, 0, 1, 2), listOf(7, 8), listOf(2, 5)))
        println(path)
    }

    @Test
    fun test3() {
        val map = disconnectedMap()

        val start = 3
        val path = PathFinder(map).findPath(start, 2, 0, listOf(listOf(3, 0, 1, 2)))
        println(path)

        val listPaths = ArrayList<List<Int>>()

        while (true) {
            val possiblePath = PathFinder(map).findPath(start, 2, 0, listPaths)
            if (!possiblePath.isEmpty())
                listPaths.add(listOf(start) + possiblePath)
            else
                break
        }

        println("k is " + listPaths.size)
        println()

        println(map.islands.size.toString() + ":")

        for (isl in map.islands) {
            println("cost " + isl.cost)
            println("mines" + isl.mines)
            println()
        }
    }

    @Test
    fun randomTest() {
        for (k in 0..0) {
            val size = 1000
            val mines = hashSetOf<Int>()

            val r = Random()
            for (i in 0..5)
                mines.add(r.nextInt(size))

            val rivers = hashSetOf<River>()
            for (i in 0..3 * size) {
                val first = r.nextInt(size)
                var second = r.nextInt(size)
                while (first == second)
                    second = r.nextInt(size)
                rivers.add(River(first, second))
            }

            val map = fromDirtyMap(rivers, mines)
            println(map.islands.size)

            val start = mines.random()
            val finish = mines.random()

            val listPaths = ArrayList<List<Int>>()

            while (true) {
                val path = PathFinder(map).findPath(start, finish, 0, listPaths)
                if (!path.isEmpty())
                    listPaths.add(listOf(start) + path)
                else
                    break
            }

            println(listPaths.size)
        }
    }
}