@file:Suppress("EXPERIMENTAL_UNSIGNED_LITERALS")

import java.io.File

fun main(args: Array<String>) {
    Problem24()
}

@ExperimentalUnsignedTypes
class Problem24 {

    fun parseInput(lines: List<String>): List<List<String>> {
        return lines.filter { line -> line.isNotBlank() }.map { line ->
            line.mineAll("(?<dir>e|se|sw|w|nw|ne)", setOf("dir")).map { m ->
                m.getOrEx("dir")
            }
        }
    }

    fun toCarth(dir: String): Pair<Int, Int> {
        return when (dir) {
            "e" -> Pair(1, 0)
            "se" -> Pair(1, -1)
            "sw" -> Pair(0, -1)
            "w" -> Pair(-1, 0)
            "nw" -> Pair(-1, 1)
            "ne" -> Pair(0, 1)
            else -> throw Exception("unrecognized $dir")
        }
    }

    fun toNeighbours(pos: Pair<Int, Int>): List<Pair<Int, Int>> {
        return listOf(
            Pair(pos.first + 1, pos.second),
            Pair(pos.first + 1, pos.second - 1),
            Pair(pos.first, pos.second - 1),
            Pair(pos.first - 1, pos.second),
            Pair(pos.first - 1, pos.second + 1),
            Pair(pos.first, pos.second + 1)
        )
    }

    fun p1(tiles: List<List<String>>): Int {
        val tileCoords = tiles.map { tileSeq ->
            tileSeq.map { s -> toCarth(s) }
                .fold(Pair(0, 0)) { acc, pair ->
                    Pair(acc.first + pair.first, acc.second + pair.second)
                }
        }

        val blackTiles = mutableSetOf<Pair<Int, Int>>()

        for (tile in tileCoords) {
            if (blackTiles.contains(tile)) {
                blackTiles.remove(tile)
            } else {
                blackTiles.add(tile)
            }
        }
        return blackTiles.size
    }

    fun flipFirst(tileCoords: List<Pair<Int, Int>>): Set<Pair<Int, Int>> {
        val blackTiles = mutableSetOf<Pair<Int, Int>>()

        for (tile in tileCoords) {
            if (blackTiles.contains(tile)) {
                blackTiles.remove(tile)
            } else {
                blackTiles.add(tile)
            }
        }
        return blackTiles
    }

    fun p2(tiles: List<List<String>>): Int {
        val tileCoords = tiles.map { tileSeq ->
            tileSeq.map { s -> toCarth(s) }
                .fold(Pair(0, 0)) { acc, pair ->
                    Pair(acc.first + pair.first, acc.second + pair.second)
                }
        }

        fun countNeighbors(plane: Set<Pair<Int,Int>>, tile: Pair<Int,Int>): Int {
            return toNeighbours(tile)
                .filter { n -> plane.contains(n) }
                .count()
        }

        var current = flipFirst(tileCoords).toSet()
        for (i in 1..100) {
            val whiteToEval = current.toList().flatMap { t -> toNeighbours(t) }.distinct()
            val remainingBlack = current.filter { blackTile ->
                val blackCount = countNeighbors(current, blackTile)
                !(blackCount == 0 || blackCount > 2)
            }
            val newBlack = whiteToEval.filter { whiteTile ->
                val blackCount = countNeighbors(current, whiteTile)
                blackCount == 2
            }

            current = (remainingBlack + newBlack).toSet()
        }

        return current.count()
    }

    init {
        val lines = File("../input24.txt").readLines()
        val exampleLines = File("../input24.txt.example").readLines()

        val input = parseInput(lines)
        val exampleInput = parseInput(exampleLines)

//        println("${parseInput(exampleLines)}")


//        println("p1: ${p1(exampleInput)}")
        println("p1: ${p2(input)}")

//        println("##### problem1: ${p1()}")
//        println("##### problem2: ${p2()}")
    }
}

