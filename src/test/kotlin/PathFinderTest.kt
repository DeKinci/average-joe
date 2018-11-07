import com.dekinci.bot.entities.River
import com.dekinci.bot.game.map.GameMap
import com.dekinci.bot.game.tactics.PathFinder
import org.junit.jupiter.api.Test
import java.util.*

class PathFinderTest {
    @Test
    fun test() {
        val size = 9
        val minesList = listOf(3, 5)
        val riverList = "0,1 1,2 0,3 0,4 1,4 4,2 2,5 3,6 6,4 7,4 4,8 8,5 6,7 7,8"
                .split(" ")
                .map { River(it.split(",")[0].toInt(), it.split(",")[1].toInt()) }

//        println(riverList)
        val map = GameMap(size, riverList, minesList)
        println(PathFinder(map).findPath(3, 5))
    }

    @Test
    fun test2() {
        val size = 10000
        val mines = hashSetOf<Int>()

        val r = Random()
        for (i in 0 .. 5)
            mines.add(r.nextInt(size))

        val rivers = hashSetOf<River>()
        for (i in 0 .. 10 * size)
            rivers.add(River(r.nextInt(size), r.nextInt(size)))

        val mineList = mines.toList()

        val map = GameMap(size, rivers.toList(), mineList)
        println(PathFinder(map).findPath(mineList[0], mineList[1]))
    }
}