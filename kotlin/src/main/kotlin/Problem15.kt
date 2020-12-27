@file:Suppress("EXPERIMENTAL_UNSIGNED_LITERALS")

fun main(args: Array<String>) {
    Problem15()
}

@ExperimentalUnsignedTypes
class Problem15 {
    init {
        val input = listOf(1, 20, 11, 6, 12, 0)

//        println("small1=${p1(listOf(0,3,6))}")

        println("result2=${p1(input)}")
    }

    fun p1(input: List<Int>): Int {
        var age = 0;
        val history = mutableMapOf<Int, List<Int>>()

        var last: Int = 0

        input.forEachIndexed { i, n ->
            history[n] = listOf(i + 1)
            last = n

        }
        age = 1 + input.size

        // age was the only diff between problems
        while (age <= 30000000) {
            // find out the new last
            val nHistory = history[last] ?: listOf()
            last = when(nHistory.size) {
                0 -> 0
                1 -> 0
                else -> nHistory.takeLast(2).last() - nHistory.first()
            }

            // update history of new last with age
            val prevHistory = history[last] ?: listOf()
            history[last] = listOf(prevHistory, listOf(age)).flatten().takeLast(2)

            age++
        }
        return last
    }
}

