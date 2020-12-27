import java.io.File

fun main() {
    Problem8()
}

class Problem8 {
    init {
        val fileInput = File("../input8.txt")
            .readLines()
            .map { s -> s.split(' ') }
            .toMutableList()

        println("p1 ${run(fileInput)}")
        val result = fix(fileInput)
        println("p2 $result")
    }

    fun run(input: List<List<String>>): Long? {
        var result: Long = 0;
        var cp: Int = 0;
        val seenIndex = mutableSetOf<Int>()
        while (cp < input.size) {
            val instruction = input[cp]
            val op = instruction[0]
            val operand = instruction[1].toInt()

            if (seenIndex.contains(cp)) {
                println("loop detected at cp=$cp, accumulator before=$result")
                return null
            }
            seenIndex.add(cp)
            when (op) {
                "nop" -> {
                    cp++
                }
                "acc" -> {
                    result += operand
                    cp++
                }
                "jmp" -> {
                    cp += operand
                }
            }
        }
        return result
    }

    fun fix(input: List<List<String>>): Long {
        var cp: Int = 0;
        while (cp < input.size) {
            val instruction = input[cp]
            val op = instruction[0]
            val operand = instruction[1].toInt()

            when (op) {
                "nop" -> {
                    val changedInput = input.toMutableList()
                    changedInput[cp] = listOf("jmp", instruction[1])

                    val result = run(changedInput)
                    if(result != null)
                        return result;
                    else {
                        cp++
                    }
                }
                "acc" -> {
                    cp++
                }
                "jmp" -> {
                    val changedInput = input.toMutableList()
                    changedInput[cp] = listOf("nop", instruction[1])

                    val result = run(changedInput)
                    if(result != null)
                        return result;
                    else {
                        cp+= operand
                    }
                }
            }
        }
        throw Exception("could not be fixed")
    }
}