@file:Suppress("EXPERIMENTAL_UNSIGNED_LITERALS")

import java.io.File

fun main(args: Array<String>) {
    Problem25()
}

@ExperimentalUnsignedTypes
class Problem25 {

    fun findLoop(pk: ULong): ULong {
        var iteration = 1uL
        var c = 1uL
        var seed = 7uL

        val history = mutableMapOf<ULong, ULong>()

        while (true) {
            c = (c * seed) % 20201227uL
            if (c == pk) {
                return iteration
            }
            iteration++
        }

    }

    fun transform(seed: ULong, iterations: ULong): ULong {
        var c = 1uL
        for (i in 1..iterations.toInt()) {
            c = (c * seed) % 20201227uL
        }
        return c
    }

    fun p1(v1: ULong, v2: ULong): String {
        val loop1 = findLoop(v1)
        val loop2 = findLoop(v2)

        println(transform(v2, loop1).toString())

        require(transform(v2, loop1) == transform(v1, loop2))

        return transform(v2, loop1).toString()
    }

    fun parseLines(lines: List<String>): Pair<ULong, ULong> {
        return Pair(
            lines[0].toULong(),
            lines[1].toULong()
        )
    }

    init {
        val lines = File("../input25.txt").readLines()
        val exampleLines = File("../input25.txt.example").readLines()


        println("##### problem1: ${p1(lines[0].toULong(), lines[1].toULong())}")

        println("##### problem1: ${p1(8uL, 7uL)}")
    }
}

