fun main() {
    Problem23()
}


class Problem23 {
    init {

        val example = "389125467".toList().map { v -> (v - '0') }
        val exampleAmount = 10
        val realAmount = 10000000

        val real = "739862541".toList().map { v -> (v - '0') }

        println("p1-example: ${p1(example, exampleAmount)}")
        println("p2-example: ${p2(example, exampleAmount)}")
//
//        println("p1-example: ${p1(real, realAmount)}")
        println("p2-example: ${p2(real, realAmount)}")

//
//        println("##### problem2: ${p2()}")
    }


    fun p1(input: List<Int>, amount: Int): List<Int> {


        val maxValue = input.maxOrNull() ?: throw Exception("not found")

        class Node(var prev: Node?, var next: Node?, val v: Int) {
            fun addAfter(c: Node): Node {
                c.next = next!!
                c.prev = this
                next!!.prev = c
                next = c

                //require(toList().reversed() == prev!!.toReversedList())

                return c
            }

            fun remove(): Node {
                val nextNode = this.next!!
                prev!!.next = next
                next!!.prev = prev
                prev = null
                next = null
                return nextNode
            }

            fun foreach(f: (Node) -> Unit) {
                var c = this
                do {
                    f(c)
                    c = c.next!!
                } while (c != this)
            }

            fun toList(): List<Int> {
                val r = ArrayDeque<Int>()
                foreach { n ->
                    r.add(n.v)
                }
                return r
            }

            fun toReversedList(): List<Int> {
                val r = ArrayDeque<Int>()
                var c = this
                do {
                    r.add(c.v)
                    c = c.prev!!
                } while (c != this)

                return r
            }
        }

        // convert the input to a double linked list and inject into map
        var first = Node(null, null, input.first())
        first.next = first
        first.prev = first

        var cur = first
        for (i in 1 until input.size) {
            val c = Node(null, null, input[i])
            cur = cur.addAfter(c)
        }

        require(first.toList() == input)
        require(first.prev!!.toReversedList() == input.reversed())

        val nodeMap = mutableMapOf<Int, Node>()
        first.foreach { n ->
            nodeMap[n.v] = n
        }

        require(first.v == input.first())
        var currentCup = input.first()
        for (i in 1..amount) {
//            if (i % 1 == 0) {
//            println("iteration $i")
//            }

            val v1 = first.next!!
            val v2 = first.next!!.next!!
            val v3 = first.next!!.next!!.next!!
            first.next!!.remove().remove().remove()
            val selectedSlice = listOf(v1, v2, v3)

//            println("currentcup = $currentCup")
            var nextCup = if (currentCup - 1 < 1) maxValue else currentCup - 1
            while (true) {
                if (selectedSlice.find { n -> n.v == nextCup } != null) {
                    nextCup = if (nextCup - 1 < 1) maxValue else nextCup - 1
                } else {
                    break
                }
            }

//            println("nextcup = $nextCup, selected: ${selectedSlice.map { v -> v.v }}")

            // move selected slice
            val nextCupNode = nodeMap[nextCup] ?: throw Exception("cannot happen")
            nextCupNode
                .addAfter(v1)
                .addAfter(v2)
                .addAfter(v3)

            first = first.next!!
            currentCup = first.v
        }

        val posOfOne = nodeMap[1]!!

        return posOfOne.remove().toList()
    }

    fun p2(input: List<Int>, amount: Int) {
        // expand list to 1 million
        val max = input.maxOrNull() ?: throw Exception("max not found")
        val nextValue = max + 1

        val bigInput = input + (nextValue..1000000)
        require(bigInput.size == 1000000)
        require(bigInput.toSet().size == 1000000)
        val solution = p1(bigInput, amount)
        println("${solution.take(10)}")
        println("p2: ${solution[0].toLong() * solution[1].toLong()}")
    }
}

