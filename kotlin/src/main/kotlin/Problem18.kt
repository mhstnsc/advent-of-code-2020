@file:Suppress("EXPERIMENTAL_UNSIGNED_LITERALS")

import java.io.File



fun main(args: Array<String>) {
    Problem18()
}

@ExperimentalUnsignedTypes
class Problem18 {
    init {
        val fileName = "../input18.txt"
        val lines = File(fileName).readLines()

        fun tokenize(expr:String): List<String> {
            return expr
                .replace(" ", "")
                .mineAll("(?<token>(\\d+)|([\\*\\+\\-()]))", setOf("token"))
                .map { m -> m.getOrEx("token") }
        }

        fun calculate(op1: String, op: String, op2: String): String {
            return when (op) {
                "+" -> op1.toLong() + op2.toLong()
                "*" -> op1.toLong() * op2.toLong()
                else -> throw Exception("unknown case $op1 $op $op2")
            }.toString()
        }

        fun p1(): Long {
            fun compute(expr: String): Long {
                val tokens = tokenize(expr)

                fun compute(it: Iterator<String>): String {
                    val stack = mutableListOf<String>()
                    fun unwind() {
                        while (stack.size >= 3) {
                            val op2 = stack.removeLast()
                            val op = stack.removeLast()
                            val op1 = stack.removeLast()
                            stack.add(calculate(op1, op, op2))
                        }
                    }
                    while (it.hasNext()) {
                        val token = it.next()
                        when (token) {
                            "*" -> stack.add(token)
                            "+" -> stack.add(token)
                            "(" -> {
                                stack.add(compute(it))
                                unwind()
                            }
                            ")" -> break
                            else -> {
                                stack.add(token)
                                unwind()
                            }
                        }
                    }
                    unwind()
                    require(stack.size == 1)
                    return stack[0]
                }

                return compute(tokens.iterator()).toLong()
            }


            return lines.map { l -> compute(l) }.sum()
        }

        fun p2(): Long {
            fun compute(expr: String): Long {
                val tokens = expr
                    .replace(" ", "")
                    .mineAll("(?<token>(\\d+)|([\\*\\+\\-()]))", setOf("token"))
                    .map { m -> m.getOrEx("token") }

                var i = 0;

                fun compute(isRightOfStar: Boolean): String {
                    val stack = mutableListOf<String>()

                    fun unwind() {

                        while (stack.size >= 3) {
                            val op2 = stack.removeLast()
                            val op = stack.removeLast()
                            val op1 = stack.removeLast()
                            stack.add(calculate(op1, op, op2))
                        }
                    }
                    while (i < tokens.size) {
                        val token = tokens[i]
                        i++
                        when (token) {
                            "*" -> {
                                unwind()
                                stack.add(token)
                                stack.add(compute(true))
                                unwind()
                            }
                            "+" -> stack.add(token)
                            "(" -> {
                                stack.add(compute(false))
                                unwind()
                            }
                            ")" -> {
                                if(isRightOfStar) {
                                    // abandon this computation and do not consume token
                                    i--
                                }
                                break;
                            }
                            else -> {
                                stack.add(token)
                            }
                        }
                    }
                    unwind()
                    require(stack.size == 1)
                    return stack[0]
                }

                return compute(false).toLong()
            }
            return lines.map { l -> compute(l) }.sum()
        }

        println("##### problem1: ${p1()}")
        println("##### problem2: ${p2()}")
    }
}

