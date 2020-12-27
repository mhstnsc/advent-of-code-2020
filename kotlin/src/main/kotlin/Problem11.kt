import java.io.File

fun main() {
    Problem11()
}

class Problem11 {

    init {
        val input = File("../input11.txt")
            .readLines()
            .filter { s -> !s.isBlank() }


        val matrix: Matrix<Char> = Matrix.from(input) { v ->
            v.toList()
        }
        //matrix.pad('.')

        val value = p1(matrix)
        println("result1 ${value}")
        val value2 = p2(matrix)
        println("result2 ${value2}")
    }

    fun p1(input: Matrix<Char>): Long {
        var result = input
        while (true) {
            val p = transform(result, 0, 4, ::countNeighborsAround)
            if (!p.first)
                break;
            result = p.second
        }

        return countOccupied(result)
    }

    fun p2(m: Matrix<Char>): Long {
        var result = m
        while (true) {
            val p = transform(result, 0, 5, ::countNeighborsFar)
            if (!p.first)
                break;
            result = p.second
        }

        return countOccupied(result)
    }

    fun transform(
        input: Matrix<Char>,
        exactlyFreeForOccupied: Int,
        atLeastOccupied: Int,
        occupiedCounter: (Matrix<Char>, Int, Int) -> Int
    ): Pair<Boolean, Matrix<Char>> {
        val result = input.copy()
        var hasChanged = false;

        for (i in 0 until result.lines()) {
            for (j in 0 until result.cols()) {
                val occupied = occupiedCounter(input, i, j)
                when (input.data[i][j]) {
                    'L' -> if (occupied == exactlyFreeForOccupied) {
                        result.data[i][j] = '#'
                        hasChanged = true;
                    }

                    '#' -> if (occupied >= atLeastOccupied) {
                        hasChanged = true
                        result.data[i][j] = 'L'
                    }
                }
            }
        }
        println()
        println(result.toString(""))
        return Pair(hasChanged, result)
    }

    fun countNeighborsFar(m: Matrix<Char>, i: Int, j: Int): Int {
        var count = 0;
        fun scanWithStep(istep: Int, jstep: Int): Int {
            var ki = i + istep
            var kj = j + jstep
            while (m.isInside(ki, kj)) {
                when(m.data[ki][kj]) {
                    '#' -> return 1
                    'L' -> return 0
                }
                ki += istep
                kj += jstep
            }
            return 0;
        }

        for (k in -1..1) {
            for (l in -1..1) {
                if (k == 0 && l == 0) continue;
                count += scanWithStep(k, l)
            }
        }
        return count
    }

    fun countNeighborsAround(m: Matrix<Char>, i: Int, j: Int): Int {
        var count = 0
        for (k in -1..1) {
            for (l in -1..1) {
                if (k == 0 && l == 0) continue;
                if(m.isInside(i+k, j+l) && m.data[i+k][j+l] == '#') {
                    count += 1
                }
            }
        }
        return count
    }

    fun countOccupied(input: Matrix<Char>): Long {
        return input.sum { v -> if (v == '#') 1 else 0 }
    }


}