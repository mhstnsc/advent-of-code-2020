@file:Suppress("EXPERIMENTAL_UNSIGNED_LITERALS")

import java.io.File

fun main(args: Array<String>) {
    Problem22()
}

@ExperimentalUnsignedTypes
class Problem22 {

    val seen = mutableSetOf<Pair<List<Long>, List<Long>>>()


    fun processInput(lines: List<String>): Pair<List<Long>, List<Long>> {
        val arrays = lines.split { s -> s.isBlank() }.map { l ->
            l.drop(1).map { s -> s.toLong() }
        }
        return Pair(arrays[0], arrays[1])
    }

    init {
        val lines = File("../input22.txt").readLines()
        val exampleLines = File("../input22.txt.example").readLines()

        seen.add(Pair(listOf(10),listOf(10)))

        val decks = processInput(lines)
        val exampleDecks = processInput(exampleLines)
//        println("##### example problem 1: ${p1(exampleDecks.first, exampleDecks.second)}")

//        println("##### problem1: ${p1(decks.first, decks.second)}")
//        println("##### problem2: ${p2()}")

//        println("example problem2: ${p2(exampleDecks.first, exampleDecks.second)}")
        println("example problem2: ${p2(decks.first, decks.second)}")
    }

    fun p1(deck1: List<Long>, deck2: List<Long>): Long {
        val player1 = ArrayDeque(deck1)
        val player2 = ArrayDeque(deck2)

        while (player1.size > 0 && player2.size > 0) {
            val cardp1 = player1.removeFirst()
            val cardp2 = player2.removeFirst()
            if (cardp1 >= cardp2) {
                player1.add(cardp1)
                player1.add(cardp2)
            } else {
                player2.add(cardp2)
                player2.add(cardp1)
            }
        }

        println("${player1 + player2}")

        val result = (player1 + player2).reversed().foldIndexed(0L) { index, acc, l ->
            acc + l * (index + 1)
        }

        return result
    }



    fun computeHash(deck1: List<Long>, deck2: List<Long>): List<Long> {
        return (deck1 + deck2).toMutableList().toList()
    }

    fun isLoop(player1: List<Long>, player2: List<Long>): Boolean {
        val e = Pair(player1, player2)
        return if(seen.contains(e)) {
            true
        } else {
            if(seen.size == 1000000) {
                println("One million combinations seen already, resetting")
                seen.clear()
            }
            seen.add(e)
            false
        }
    }

    fun subGame(deck1: List<Long>, deck2: List<Long>): Int {

        val seenSubGame = mutableSetOf<Pair<List<Long>, List<Long>>>()

        fun isLoop(player1: List<Long>, player2: List<Long>): Boolean {
            val e = Pair(player1, player2)
            return if(seenSubGame.contains(e)) {
                true
            } else {
                if(seenSubGame.size == 1000000) {
                    println("One million combinations seen already, resetting")
                    seenSubGame.clear()
                }
                seenSubGame.add(e)
                false
            }
        }

        val player1 = ArrayDeque(deck1)
        val player2 = ArrayDeque(deck2)

        while (player1.size > 0 && player2.size > 0) {
            val cardp1 = player1.removeFirst()
            val cardp2 = player2.removeFirst()

            if(isLoop(player1.toList(), player2.toList())) {
                player1.addFirst(cardp1)
                player1.addFirst(cardp2)
                continue
            }

            if (cardp1 >= cardp2) {
                player1.add(cardp1)
                player1.add(cardp2)
            } else {
                player2.add(cardp2)
                player2.add(cardp1)
            }
        }

        require(player1.size == 0 || player2.size == 0)

        return if (player1.size == 0) 2 else 1
    }

    fun p2(deck1: List<Long>, deck2: List<Long>): Long {
        val player1 = ArrayDeque(deck1)
        val player2 = ArrayDeque(deck2)

        while (player1.size > 0 && player2.size > 0) {
            val cardp1 = player1.removeFirst()
            val cardp2 = player2.removeFirst()

            if(isLoop(player1.toList(), player2.toList())) {
                player1.addFirst(cardp1)
                player1.addFirst(cardp2)
                continue
            }

            if (player1.size >= cardp1 && player2.size >= cardp2) {
                val p1copy = player1.subList(0, cardp1.toInt()).toList()
                val p2copy = player2.subList(0, cardp2.toInt()).toList()

                println("subgame for ${cardp1} vd ${cardp2}")
                println("deck1: ${player1}")
                println("deck2: ${player2}")
                val subgame = subGame(p1copy, p2copy)
                if (subgame == 1) {
                    player1.add(cardp1)
                    player1.add(cardp2)
                } else {
                    player2.add(cardp2)
                    player2.add(cardp1)
                }
            } else {
                if (cardp1 >= cardp2) {
                    player1.add(cardp1)
                    player1.add(cardp2)
                } else {
                    player2.add(cardp2)
                    player2.add(cardp1)
                }
            }
        }


        val result = (player1 + player2).reversed().foldIndexed(0L) { index, acc, l ->
            acc + l * (index + 1)
        }
        return result
    }
}

