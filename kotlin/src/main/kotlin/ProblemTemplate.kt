@file:Suppress("EXPERIMENTAL_UNSIGNED_LITERALS")

import java.io.File

fun main() {

    val lines = File("../inputXX.txt").readLines()
    val exampleLines = File("../input22.txt.example").readLines()

    println("Parse control: ${parseInput(exampleLines)}")

    val input = parseInput(lines)

    println("p1 example: ${p1(exampleLines)}")
//    println("p1: ${p1(lines)}")
//    println("p2 example: ${p2(exampleLines)}")
//    println("p2: ${p2(lines)}")
}

fun parseInput(lines: List<String>): Any {
    return Any()
}

fun p1(lines: List<String>): String {
    val input = parseInput(lines)

    return "not solved"
}

fun p2(lines:List<String>): String {
    val input = parseInput(lines)

    return "not solved"
}
