@file:Suppress("EXPERIMENTAL_UNSIGNED_LITERALS")

import java.io.File

fun main(args: Array<String>) {
    Problem19()
}


@ExperimentalUnsignedTypes
class Problem19 {
    class Rule(
        val ruleId: Long,
        val char: Char?,
        val orList: List<List<Long>>?
    ) {
        override fun toString(): String {
            if(char != null ){
                return "$ruleId: $char"
            } else {
                return "$ruleId: $orList"
            }
        }
    }

    class Matcher(val rules: Map<Long, Rule>) {
        fun andMatcher(andRule: List<Long>, target: String, i: Int): List<Pair<Boolean, Int>> {
            if (andRule.isEmpty()) {
                return listOf(Pair(true, i))
            }
            val ruleTail = andRule.drop(1)

            val matchResult = match(andRule[0], target, i)
                .filter { p -> p.first }
                .map { p ->
                    andMatcher(ruleTail, target, p.second)
                }.flatten()

            return matchResult
        }

        fun match(ruleId: Long, target: String, i: Int): List<Pair<Boolean, Int>> {
            val rule = rules[ruleId] ?: throw Exception("rule $ruleId not found")
            if (i >= target.length)
                return listOf()

            if (rule.char != null) {
                return if (target[i] == rule.char)
                    listOf(Pair(true, i + 1))
                else
                    listOf()
            } else if (rule.orList != null) {
                val orMatcher = rule
                    .orList
                    .map { andRule ->
                        andMatcher(andRule, target, i)
                            .filter { p -> p.first }
                    }
                    .flatten()
                return orMatcher
            } else {
                throw Exception("cannot process $rule")
            }
        }
    }

    var rules: Map<Long, Rule> = mapOf()

    init {
        val fileName = "../input19.txt"
        val lines = File(fileName).readLines().split { s -> s.isBlank() }
        val inputRules = lines[0]
        val targets = lines[1]

        rules = inputRules.map { rule ->
            val splitRule = rule.split(":").map { s -> s.trim() }
            val ruleId = splitRule[0].toLong()
            val ruleSpec = splitRule[1]
            if (ruleSpec.startsWith("\"")) {
                Pair(ruleId, Rule(ruleId, ruleSpec[1], null))
            } else {
                val orList = ruleSpec.split("|").map { and ->
                    and.trim().split(" ").map { strv -> strv.toLong() }
                }
                Pair(ruleId, Rule(ruleId, null, orList))
            }
        }.toMap()
        rules.toSortedMap().printFully("parsed rules")

        fun matchTargets(matcher: Matcher): Long {
            val r = targets.map { s ->
                println("matching $s")
                matcher.match(0, s, 0).filter { p -> p.first && p.second == s.length }.isNotEmpty()
            }.count { isMatched -> isMatched }

            return r.toLong()
        }

        fun test(): Long {
            val mutableRules = rules.toMutableMap()
            mutableRules[8] = Rule(8, null, listOf(listOf(42), listOf(42, 8)))
            mutableRules[11] = Rule(11, null, listOf(listOf(42, 31), listOf(42, 11, 31)))
            rules = mutableRules.toMap()

            return matchTargets(Matcher(rules))
        }

//        println("test: ${test()}")



        fun toRegex(ruleId: Long): String {
            val rule = rules[ruleId] ?: throw Exception("rule $ruleId not found")
            if (rule.char != null) {
                return "${rule.char}"
            } else if (rule.orList != null) {
                val orMatcher = rule.orList.map { andRule ->
                    andRule.map { ruleId ->
                        toRegex(ruleId)
                    }.joinToString("")
                }.map { s -> "(${s})" }.joinToString("|")
                return "($orMatcher)"
            } else {
                throw Exception("cannot process $rule")
            }
        }

        fun p1(): Long {
            val matcherRegex = toRegex(0)
            println("final matcher: $matcherRegex")

            val matcher = Regex(matcherRegex)

            val r = targets.map { s ->
                matcher.matchEntire(s) != null
            }.count { isMatched ->
                isMatched
            }

            return r.toLong()
        }


        fun p2(): Long {

            val mutableRules = rules.toMutableMap()
            mutableRules[8] = Rule(8, null, listOf(listOf(42), listOf(42, 8)))
            mutableRules[11] = Rule(11, null, listOf(listOf(42, 31), listOf(42, 11, 31)))
            rules = mutableRules.toMap()

            val matcher = Matcher(rules)

            return matchTargets(matcher)
        }


        println("##### problem1: ${p1()}")
        println("##### problem2: ${p2()}")
    }


}

