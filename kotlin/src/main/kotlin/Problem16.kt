@file:Suppress("EXPERIMENTAL_UNSIGNED_LITERALS")

import java.io.File

fun main(args: Array<String>) {
    Problem16()
}

@ExperimentalUnsignedTypes
class Problem16 {
    init {
        val lines =  File("../input16.txt").readLines()

        val zones = lines.split { s -> s.isBlank() }.filter { l -> l.isNotEmpty() }

        fun loadIntervals(l: List<String>): Map<String, List<Pair<Long, Long>>> {
            return l.map { s ->
                val classM = s.mineOne("(?<class>\\w+):")
                val intervalM = s.mineAll("(?<start>\\d+)-(?<end>\\d+)").map { m ->
                    Pair(
                        m.getOrEx("start").toLong(),
                        m.getOrEx("end").toLong()
                    )
                }

                val name = classM.getOrEx("class")
                Pair(name, intervalM)
            }.toMap()
        }

        val departures = loadIntervals(zones[0].filter { s -> s.contains("departure") })
        val arrivals = loadIntervals(zones[0].filter { s -> s.contains("arrival") })
        val others = loadIntervals(
            zones[0].filterNot { s -> s.contains("departure") || s.contains("arrival") }
        )
        val yourTicket = zones[1][1].splitToLong(",")
        val otherTickets = zones[2].drop(1).map { s -> s.splitToLong(",") }

        departures.printSample("departure")
        arrivals.printSample("arrival")
        others.printSample("other")
        yourTicket.printFully("your ticket")
        otherTickets.printSample("nearby tickets")


        fun p1(): Long {
            // merge all intervals
            val allIntervals = listOf(departures.values, arrivals.values, others.values).flatten().flatten()

            val allValues = listOf(yourTicket, otherTickets.flatten()).flatten()

            // search

            return allValues.filter { v ->
                val foundPair = allIntervals.find { p -> p.first <= v && v <= p.second }
                foundPair == null
            }.sum()
        }

        fun p2(): Long {
            // filter out nearby tickets of invalid values
            val allIntervals = listOf(departures.values, arrivals.values, others.values).flatten().flatten()
            val allNamedIntervals = listOf(
                departures.toList().map {
                    p -> Pair(p.first + " departure", p.second)
                },
                arrivals.toList(),
                others.toList()
            ).flatten()

            val validTickets: List<List<Long>> = otherTickets.filter { l ->
                l.filter { v ->
                    val foundPair = allIntervals.find { p -> p.first <= v && v <= p.second }
                    foundPair != null
                }.size == l.size
            }
            println("validtickets: ${validTickets.size}")

            fun findAllFields(v: Long): List<String> {
                return allNamedIntervals.filter {
                    namedInterval ->
                        val foundInterval = namedInterval.second.find {
                                interval -> interval.first <= v && v <= interval.second
                        }
                    foundInterval != null
                }.map {
                    p -> p.first
                }
            }

            val indexToNames = mutableMapOf<Int, List<String>>()
            for(i in validTickets[0].indices) {
                val column = validTickets.map { l -> l[i] }
                val fieldNames = column.map {
                    v -> findAllFields(v)
                }.map { v -> v.toSet() }

                val allNames = fieldNames.fold(setOf<String>()) {
                    acc, set -> acc union set
                }
                val theName = fieldNames.fold(allNames) {
                    acc, set -> acc intersect set
                }
                indexToNames[i] = theName.toList()
            }

            val sortedIndexToNames = indexToNames
                .toList()
                .sortedBy {
                        e -> e.second.size
                }
                .map { e -> Pair(e.first, e.second.toMutableSet()) }

            sortedIndexToNames.toMap().printFully("sortedIndexToNames")

            val filterMask = mutableSetOf<String>()

            for(i in 0 until sortedIndexToNames.size - 1) {
                require(sortedIndexToNames[i].second.size == 1)
                filterMask.add(sortedIndexToNames[i].second.first())
                for(j in i+1 until sortedIndexToNames.size) {
                    sortedIndexToNames[j].second.removeAll(filterMask)
                }
            }

            sortedIndexToNames.toMap().printFully("sortedIndexToNames")

            val fieldOrder = sortedIndexToNames.sortedBy { e -> e.first }.map { e -> e.second.first() }

            // based on order and your ticket, filter interesting value and multiply numbers
            val depatureField = yourTicket.zip(fieldOrder)
                .filter {
                        p -> p.second.contains("departure")
                }
            require(depatureField.size == 6)

            val result = depatureField
                .map {
                    p -> p.first
                }.fold(1L) {
                    acc, l -> acc * l
                }

            return result
        }

        println("problem1: ${p1()}")
        println("problem2: ${p2()}")
    }
}

