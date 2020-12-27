import java.io.File


class Problem7 {
    init {
        val lines = File("../input7.txt").readLines()
        class Contents(val count: Int?, val bagId: String?) {
            override fun toString(): String {
                return "$bagId=$count"
            }
        }

        val rules: MutableMap<String, List<Contents>> =
            lines
                .mineOne("(?<bagId>\\w+ \\w+)").map { v -> v["bagId"]?:"" }
                .filter { v -> v.isNotBlank() }
                .zip(
                    lines.mineAll("(?<count>\\d+) (?<bagId>\\w+ \\w+) bags?")
                )
                .map { p ->
                    Pair(
                        p.first,
                        p.second.map { v ->
                            Contents(v["count"]?.toInt(), v["bagId"])
                        }
                    )
                }
                .toMap()
                .toMutableMap()

        fun countAncestors(containedBagId: String): Int {
            val ancestors = mutableSetOf<String>()

            fun isAncestor(bagId: String): Boolean {
                return if (ancestors.contains(bagId))
                    true
                else {
                    if (bagId == containedBagId) {
                        true
                    } else {
                        val entry = rules[bagId]!!
                        val mapped = entry.map { s -> if (s.bagId != null) isAncestor(s.bagId) else false }
                        mapped.fold(false) { acc, b -> acc || b }
                    }
                }
            }

            for (bagId in rules.keys) {
                if (isAncestor(bagId)) {
                    ancestors.add(bagId)
                }
            }
            return ancestors.size
        }

        fun countBags(bagId: String): Int {
            val sum = rules[bagId]?.map { e ->
                e.count?.times(countBags(e.bagId!!))
            }?.map { v -> v ?: 0 }?.sum()
            return sum?.plus(1) ?: 0
        }
        println("${countAncestors("shiny gold") - 1}")
        println("${countBags("shiny gold") - 1}")
    }
}

fun main(args: Array<String>) {
    Problem7()
}