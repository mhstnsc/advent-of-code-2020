import java.io.File

fun main(args: Array<String>) {
    Problem12()
}

class Problem12 {
    init {
        val lines = File("../input.txt")
            .readLines()

        // comment out if no regex parsing
        val command = "command"
        val value = "value"
        val mined = lines.mineOne("(?<$command>\\w)(?<value>\\d+)")
            .map { e -> Pair((e[command] ?: "")[0], e[value]?.toInt() ?: 0) }

        println("${p1(mined)}")
        println("${p2(mined)}")
    }

    class Pos(val x: Long, val y: Long, val angle: Int) {
        fun moveForward(value: Int): Pos {
            return when (angle) {
                0 -> Pos(x, y + value, angle)
                90 -> Pos(x + value, y, angle)
                180 -> Pos(x, y - value, angle)
                270 -> Pos(x - value, y, angle)
                else -> throw Exception("Unhandle angle $angle")
            }
        }

        fun translate(direction: Int, value: Int): Pos {
            return when (direction) {
                0 -> Pos(x, y + value, angle)
                90 -> Pos(x + value, y, angle)
                180 -> Pos(x, y - value, angle)
                270 -> Pos(x - value, y, angle)
                else -> throw Exception("Unhandle angle")
            }
        }

        fun rotate(angleDiff: Int): Pos {
            val normalizedAngle = (angleDiff + 360) % 360
            return Pos(x, y, (angle + normalizedAngle) % 360)
        }
    }


    class PosW(val x: Long, val y: Long, val angle: Int, val wx: Long, val wy: Long) {
        fun moveForward(value: Int): PosW {
            return PosW(x + wx * value, y + wy * value, angle, wx, wy)
        }

        fun translate(direction: Int, value: Int): PosW {
            return when (direction) {
                90 -> PosW(x, y, angle, wx + value, wy)
                270 -> PosW(x, y, angle, wx - value, wy)
                0 -> PosW(x, y, angle, wx, wy + value)
                180 -> PosW(x, y, angle, wx, wy - value)
                else -> throw Exception("Unhandle angle $direction")
            }
        }

        fun rotate(angleDiff: Int): PosW {
            val positiveRotation = (angleDiff + 360) % 360


            return when(positiveRotation) {
                0 -> this
                90 -> PosW(x,y,angle, wy, -wx)
                180 -> PosW(x,y,angle, -wx, -wy)
                270 -> PosW(x,y,angle, -wy, wx)
                else -> throw Exception()
            }
        }

        override fun toString(): String {
            return "$x $y $angle $wx $wy"
        }
    }

    fun p1(cmds: List<Pair<Char, Int>>): Long {
        val finalPos = cmds.fold(Pos(0, 0, 90)) { acc, pair ->
            when (pair.first) {
                'F' -> acc.moveForward(pair.second)
                'L' -> acc.rotate(-pair.second)
                'R' -> acc.rotate(pair.second)
                'N' -> acc.translate(0, pair.second)
                'S' -> acc.translate(180, pair.second)
                'E' -> acc.translate(90, pair.second)
                'W' -> acc.translate(270, pair.second)
                else -> throw Exception("unhandled command $pair")
            }
        }
        return Math.abs(finalPos.x) + Math.abs(finalPos.y)
    }

    fun p2(cmds: List<Pair<Char, Int>>): Long {
        val finalPos = cmds.fold(PosW(0, 0, 90, 10, 1)) { acc, pair ->
            val r = when (pair.first) {
                'F' -> acc.moveForward(pair.second)
                'L' -> acc.rotate(-pair.second)
                'R' -> acc.rotate(pair.second)
                'N' -> acc.translate(0, pair.second)
                'S' -> acc.translate(180, pair.second)
                'E' -> acc.translate(90, pair.second)
                'W' -> acc.translate(270, pair.second)
                else -> throw Exception("unhandled command $pair")
            }
            println("$r")
            r
        }
        return Math.abs(finalPos.x) + Math.abs(finalPos.y)
    }
}

