/**
 * A multidimensional game of life
 */
fun binaryGameOfLife(
    state: Set<List<Int>>,
    aliveTransform: (Int) -> Boolean,
    deadTransform: (Int) -> Boolean
): Set<List<Int>> {
    val existingTransformed = state.filter { p ->
        aliveTransform(
            p.neighbours().filter { p -> state.contains(p) }.count()
        )
    }
    val newTransformed = state
        .map { p -> p.neighbours() }        // get neighbours of alive
        .flatten()
        .filter { p -> !state.contains(p) } // keep only neighbours which are not alive
        .toSet()
        .filter { p ->                      // take the neighbours of the not alive and count the alive
            deadTransform(
                p.neighbours().filter { n -> state.contains(n) }.count()
            )
        }
    return (existingTransformed + newTransformed).toSet()
}

fun <T> generalGameOfLife(
    state: Map<List<Int>, T>,
    transform: (T?, List<T>) -> T?,
): Map<List<Int>, T> {

    val transformer = { p: List<Int> ->
        val newVal = transform(
            state.get(p),
            p.neighbours().map { p -> state.get(p) }.filter { v -> v != null }.map { v -> v!! }
        )
        if(newVal != null) {
            Pair<List<Int>, T>(p, newVal)
        } else null
    }

    val existingTransformed = state.keys.mapNotNull(transformer).toMap()

    val newTransformed = state
        .map { p -> p.key.neighbours() }        // get neighbours of alive
        .flatten()
        .filter { p -> !state.contains(p) } // keep only neighbours which are not in the map
        .toSet()
        .mapNotNull(transformer)
        .toMap()

    return existingTransformed + newTransformed
}