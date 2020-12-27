import java.io.File
import java.util.stream.IntStream.range
import kotlin.streams.toList

fun main() {
    Problem5()
}

class Problem5 {
    init {
        val seatIds: List<Int> = File("../input5.txt")
            .readLines()
            .map { seatSpec -> toSeatId(seatSpec) }

        println("problem5-first: ${seatIds.maxOrNull()}")

        val remainingSeats: List<Int> = (range(0, 128 * 8).toList() subtract seatIds).toList()
        println("remaining seats = $remainingSeats")

        for (i in 0..remainingSeats.size) {
            if (remainingSeats[i] + 1 < remainingSeats[i + 1]) {
                println("my seatid: ${remainingSeats[i + 1]}")
                break;
            }
        }
    }

    class Acc(val rowLow: Int, val rowHigh: Int, val seatLow: Int, val seatHigh: Int) {
        fun narrow(c: Char): Acc {
            return when (c) {
                'F' -> Acc(rowLow, (rowHigh + rowLow) / 2, seatLow, seatHigh)
                'B' -> Acc((rowHigh + rowLow) / 2, rowHigh, seatLow, seatHigh)
                'L' -> Acc(rowLow, rowHigh, seatLow, (seatHigh + seatLow) / 2)
                'R' -> Acc(rowLow, rowHigh, (seatHigh + seatLow) / 2, seatHigh)
                else -> throw Exception("unexpected case)")
            }
        }

        fun toSeatId(): Int {
            return rowLow * 8 + seatHigh - 1
        }
    }

    private fun toSeatId(seatSpec: String): Int {
        return seatSpec.fold(Acc(0, 128, 0, 8)) { acc, c ->
            acc.narrow(c)
        }.toSeatId()
    }
}