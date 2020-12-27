fun <T> List<T>.printFully(msg: String) {
    println("$msg: [${this.size}] ${this[0]}")
}

fun <T> List<T>.printSample(msg: String) {
    println("$msg SAMPLE: [${this.size}]${this[0]}")
}

fun <K, V> Map<K, V>.printSample(msg: String) {
    println("$msg SAMPLE: [${this.size}] ${this.iterator().next()}")
}

fun <K, V> Map<K, V>.printFully(msg: String) {
    for (e in this) {
        println("$msg: ${e.key} -> ${e.value}")
    }
}

fun <T> List<List<T>>.render(separator: String = "", indentation: Int = 4): String {
    return " ".repeat(indentation) + if (this.isEmpty()) {
        "<empty>"
    } else {
        this.joinToString("\n" + " ".repeat(indentation)) { line ->
            line.joinToString(separator)
        }
    }
}

fun <T> List<T>.render(indentation: Int = 4): String {
    return " ".repeat(indentation) + if (this.isEmpty()) {
        "<empty>"
    } else {
        this.joinToString("\n" + " ".repeat(indentation))
    }
}

fun <K, V> Map<K, V>.render(): String {
    return this.map { e ->
        "${e.key} -> ${e.value}"
    }.render()
}
