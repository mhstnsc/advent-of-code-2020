/**
 * Splits a list into pieces based on a check if a list has a separator
 */
fun <T> List<T>.split(separator: (T) -> Boolean): List<List<T>> {
    val result = mutableListOf<List<T>>()
    var current = mutableListOf<T>()
    for (e in this) {
        if (separator(e)) {
            result.add(current.toList())
            current = mutableListOf()
        } else {
            current.add(e)
        }
    }
    result.add(current.toList())
    return result.toList()
}

fun String.splitToLong(separator: String): List<Long> {
    return this.split(separator).map(String::toLong)
}
