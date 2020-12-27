@file:Suppress("EXPERIMENTAL_UNSIGNED_LITERALS")

import java.io.File

fun main(args: Array<String>) {
    Problem14()
}

@ExperimentalUnsignedTypes
class Problem14 {
    init {
        val lines = File("../input14.txt")
            .readLines()

        println("result1=${p1(lines)}")
        println("result1=${p2(lines)}")
    }

    fun expand(addr: Long, mask: String): List<Long> {
        println("${addr.toString(2)} ${mask.reversed()}")
        var result = listOf<Long>(0)
        for(bitIdx in 0 until 36) {
            when(mask[bitIdx]) {
                '0' -> result = result.map {
                    prev -> prev or (addr and (1L shl bitIdx))
                }
                '1' -> result = result.map {
                    prev -> prev or (1L shl bitIdx)
                }
                'X' -> result = result.flatMap {
                    prev -> listOf(
                        prev, prev or (1L shl bitIdx)
                    )
                }
                else -> throw Exception("$mask $bitIdx")
            }
        }

        println("$result")

        return result
    }

    fun p2(lines: List<String>): ULong {
        val memory = mutableMapOf<Long, ULong>()

        var mask = ""
        for (line in lines) {
            val maskResult = line.mineOne("mask ?= ?(?<mask>[X01]+)")?.get("mask")?.reversed()
            if (maskResult != null) {
                mask = maskResult
                continue;
            } else {
                val m = line.mineOne("mem\\[(?<addr>\\d+)\\] = (?<value>\\d+)")
                    ?: throw Exception(line)
                val addr = m["addr"]?.toLong() ?: throw Exception(line)
                val value = m["value"]?.toULong() ?: throw Exception(line)

                for(newAddr in expand(addr, mask)) {
                    memory[newAddr] = value
                }
            }
        }

        return memory.values.sum()
    }

    fun p1(lines: List<String>): ULong {
        var masks = Pair(0uL, 0uL)
        val memory = mutableMapOf<Long, ULong>()

        for (line in lines) {
            val mask = line.mineOne("mask ?= ?(?<mask>[X01]+)")?.get("mask")
            if (mask != null) {
                masks = extractMasks(mask)
                println("orMask:${masks.first.toString(2)} andMask: ${masks.second.toString(2)}")
                continue;
            } else {
                val m = line.mineOne("mem\\[(?<addr>\\d+)\\] = (?<value>\\d+)")
                    ?: throw Exception(line)
                val addr = m["addr"]?.toLong() ?: throw Exception(line)
                val value = m["value"]?.toULong() ?: throw Exception(line)
                memory[addr] = value or masks.first and masks.second
            }
        }

        return memory.values.sum()
    }

    @ExperimentalUnsignedTypes
    fun extractMasks(s: String): Pair<ULong, ULong> {

        val allone = (-1L).toULong()

        // makes everything 1
        val orMask = s.reversed().replace("X", "0").mapIndexed { idx, c ->
            when (c) {
                '0' -> 0uL
                '1' -> 1uL shl idx
                else -> throw Exception("missing case")
            }
        }.fold(0uL) { acc, l ->
            acc or l
        }

        // 0 in position the rest 1
        val andMask = s.reversed().replace("X", "1").mapIndexed { idx, c ->
            when (c) {
                '0' -> (1uL shl idx) xor allone
                '1' -> allone
                else -> throw Exception("missing case")
            }
        }.fold(allone) { acc, l ->
            acc and l
        }
        return Pair(orMask, andMask)
    }
}

