import java.io.File

fun main() {
    Problem9()
}

class Problem9 {
    init {
        val input = File("../input9.txt")
            .readLines()
            .map { s -> s.toLong() }

        val sum2fault = p1(input, 25)

        val result = p2(input, sum2fault)
        println("Control sum: ${result.sum()}")
        println("Sum min-max: ${result.minOrNull()?.plus(result.maxOrNull()?:0) }")
    }

    fun sum2(input: List<Long>, sum: Long): Boolean {
        val set = input.toSet()
        for(v in input) {
            if(sum - v == v)
                continue;
            if(set.contains(sum-v))
                return true
        }
        return false;
    }
    fun p1(input: List<Long>, windowSize: Int): Long {
        var currentIndex = windowSize
        while(currentIndex < input.size) {
            val subInput = input.subList(currentIndex - windowSize, currentIndex)
            if(!sum2(subInput, input[currentIndex])) {
                println(input[currentIndex])
                return input[currentIndex];
            }
            currentIndex++;
        }
        throw Exception("should not happen")
    }

    fun p2(input: List<Long>, searched: Long): List<Long> {
        var start = 0
        var end = 1
        var sum = input[start] + input[end]
        while (true) {
            if (sum == searched)
                return input.subList(start, end+1)
            if (sum < searched) {
                // extend rankge
                end++;
                if (end == input.size)
                    break;
                sum += input[end]
            } else {
                // shrink range
                if (end - start > 2) {
                    sum -= input[start]
                    start++
                }
            }
        }
        return listOf()
    }
}