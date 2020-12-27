@file:Suppress("EXPERIMENTAL_UNSIGNED_LITERALS")

import java.io.File

fun main(args: Array<String>) {
    Problem13()
}

@ExperimentalUnsignedTypes
class Problem13 {
    init {
        val lines = File("../input13.txt")
            .readLines()

        val earliest = lines[0].toInt()
        println("result1=${p2(lines[1])}")
    }

    class Bus(val busId: Long, val diffPrev: Long)

    class Solution(val initial: Long, val loop: Long) {
        override fun toString(): String {
            return "initial:${initial} loop:${loop}"
        }
    }

    fun p2(schedule: String): Long {
        val busses = schedule.split(",")
            .mapIndexed { i, busId -> Pair(busId, i) }
            .filter { p -> p.first != "x" }
            .map { p -> Bus(p.first.toLong(), p.second.toLong()) }

        var acc = Solution(0, busses[0].busId)
        var offset: Long
        for(i in 1 until busses.size) {
            offset = if(acc.initial % busses[i].busId == 0L) 0L else (busses[i].busId - acc.initial % busses[i].busId)
            val nextSolution = solveStep(acc.loop, offset, busses[i])
            acc = merge(acc, nextSolution)
        }
        return acc.initial
    }

    fun merge(acc: Solution, c: Solution): Solution {
        return Solution(acc.initial + c.initial, c.loop)
    }

    fun solveStep(size1: Long, initial2: Long, bus: Bus): Solution {
        // find out the new loop count by adding one loop count until they match again
        val newInitial = solveFromDiffToDiff(size1, initial2, bus.busId, bus.diffPrev)
        require(newInitial % size1 == 0L)

        val newLoop = solveFromDiffToDiff(size1, bus.diffPrev, bus.busId, bus.diffPrev)
        require(newLoop % size1 == 0L)
        require((newLoop + newInitial) % size1 == 0L)

        return Solution(newInitial, newLoop)
    }

    fun fill(from: Long, to: Long, step: Long): Long {
        val diffToCover = to - from
        return diffToCover / step + if(diffToCover % step != 0L) 1L else 0L
    }

    fun solveFromDiffToDiff(step1: Long, initialDiff: Long, step2: Long, targetDiff: Long): Long {
        var sum1 = 0L
        var sum2 = initialDiff + step2
        require(sum1 < sum2)
        require(step1 > 0)
        require(step2 > 0)
        var prevDiff = sum2 - sum1
        while(sum1 < sum2) {
            if(sum1 + targetDiff == sum2) {
                break
            }
            sum1 += step1
            if(sum1 >= sum2) {
                    sum2 += step2 * fill(sum2, sum1 + targetDiff, step2)
                    require(sum1 < sum2, { "$sum1 < $sum2" } )
            }
            require(prevDiff != sum2 - sum1)
        }
        return sum1
    }

    fun p1(earliest: Int, schedule: String): Int {
        val busses = schedule
            .split(",")
            .filter { p -> p != "x" }
            .map { x -> x.toInt() }
        println("$earliest $busses")


        val max = busses.map {
            v -> Pair(v, (v * ((earliest / v) + 1)) - earliest)
        }.fold(Pair(-1, Int.MAX_VALUE)) {
            acc, pair -> if(acc.second >= pair.second) pair else acc
        }

        println(max)

        return max.first * max.second
    }
}

