@file:Suppress("EXPERIMENTAL_UNSIGNED_LITERALS")

import java.io.File

fun main(args: Array<String>) {
    Problem17p2()
}

@ExperimentalUnsignedTypes
class Problem17p2 {

    data class Point(
        val x: Int,
        val y: Int,
        val z: Int,
        val t: Int
    ) {
        override fun toString(): String {
            return "($x,$y,$z, $t)"
        }
    }

    fun parseInput(input: List<String>): Set<Point> {
        return input.mapIndexed { y, s ->
            s.mapIndexed { x, c ->
                if (c == '#') Point(x, y, 0, 0) else null
            }
        }.flatten().filter { p -> p != null }.map { p -> p!! }.toSet()
    }

    fun Point.neighbours(): List<Point> {
        return (-1..1).map { t ->
            (-1..1).map { z ->
                (-1..1).map { y ->
                    (-1..1).map { x ->
                        Point(this.x + x, this.y + y, this.z + z, this.t + t)
                    }
                }
            }
        }.flatten().flatten().flatten().filter { p -> p != this }
    }

    fun transform(space: Set<Point>): Set<Point> {
        val existingTransformed = space.filter { p ->
            val aroundCount = p.neighbours().filter { np -> space.contains(np) }.count()
            aroundCount == 2 || aroundCount == 3
        }.toSet()

        val newTransformed = space
            .map { p -> p.neighbours() }
            .flatten()
            .toSet()
            .filter { p ->
                val aroundCount = p.neighbours().filter { np -> space.contains(np) }.count()
                aroundCount == 3
            }.toSet()

        return existingTransformed + newTransformed
    }

    fun Set<Point>.render(): String {
        return "\t" + this.joinToString("\n\t")
    }

    fun p2(lines: List<String>): String {
        var space = parseInput(lines)

        for (i in 1..6) {
//            println("$i -> \n${space.render()}")
            space = transform(space)
        }

        return space.size.toString()
    }

    init {
        val exampleInput = File("/Users/mihai/projects/advent-of-code-2020/input17.txt.small").readLines()
            .filter { s -> s.isNotBlank() }
        val input =
            File("/Users/mihai/projects/advent-of-code-2020/input17.txt").readLines().filter { s -> s.isNotBlank() }

        println("##### problem2: ${p2(input)}")
    }
}

