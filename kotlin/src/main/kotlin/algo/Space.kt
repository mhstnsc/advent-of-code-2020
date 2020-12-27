import algo.backtracking.findAll

/**
 * generate all neighbours in N-dimensional space
 */
fun List<Int>.neighbours(): Set<List<Int>> {

    val isComplete = { i: List<Int> -> i.size == this.size }

    // generate all things around
    val relativeNeighbours = findAll(listOf(), isComplete) { currentList ->
        (-1..1).map { v -> currentList + v }
    }

    return relativeNeighbours
        .map { result -> (result zip this).map { p -> p.first + p.second } } // make absolute coords
        .filter { n -> n != this }
        .toSet()// eliminate itself
}
