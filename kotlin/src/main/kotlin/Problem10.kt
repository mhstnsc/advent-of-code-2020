import java.io.File
import java.lang.Integer.max
import java.util.stream.IntStream.range

fun main() {
    Problem10()
}

class Problem10 {
    init {
        val input = File("../input10.txt")
            .readLines()
            .map { s -> s.toLong() }
            .sorted()

        val value = p1(input)
        println("ersult1 ${value}")
        println("result2: ${p2(input)}")
    }

    fun p1(input: List<Long>): Long {
        var diff1: Long = 0
        var diff3: Long = 0
        var prev = 0L;
        for(i in range(0, input.size)) {
            println("${prev} ${input[i]}")
            if(input[i] - prev == 1L)
                diff1++
            if(input[i] - prev == 3L)
                diff3++
            prev = input[i]
        }
        println("${diff1}, $diff3")
        return diff1 * (diff3+1)
    }

    fun p2(input: List<Long>):Long {
        val l = input.toMutableList()
        l.add(0, 0L)
        l.add(input.size, (input.maxOrNull()?:0L)+3)

        val posToCount = mutableMapOf<Int, Long>()
        posToCount[0] = 1
        for(i in 1 until l.size) {
            var countOfPos = 0L
            for(j in max(i-4, 0) until i) {
                if(l[i]-l[j] <=3 ) {
                    countOfPos += posToCount[j]?:0
                }
            }
            posToCount.put(i, countOfPos)
        }
        return posToCount[l.size-1]?:0
    }
}