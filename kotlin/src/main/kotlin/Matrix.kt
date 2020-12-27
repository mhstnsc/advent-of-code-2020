class Matrix<T>(val data: MutableList<MutableList<T>>) {
    companion object Factory {
        fun <X, T> from(input: List<X>, mapper: (X) -> List<T>): Matrix<T> {
            return Matrix(
                input.map { line ->
                    mapper(line).toMutableList()
                }.toMutableList()
            )
        }
    }

    fun lines(): Int {
        return data.size
    }

    fun cols(): Int {
        return if (data.isEmpty()) 0 else data[0].size
    }

    fun copy(): Matrix<T> {
        val result = mutableListOf<MutableList<T>>()
        for (e in data) {
            result.add(e.toList().toMutableList())
        }
        return Matrix(result)
    }

    fun pad(value: T) {
        val wholeLine = (0 until cols() + 2).map { _ -> value }
        for (e in data) {
            e.add(0, value)
            e.add(value)
        }
        data.add(0, wholeLine.toMutableList())
        data.add(wholeLine.toMutableList())
    }

    fun sum(pred: (T)->Long): Long {
        return data
            .map { e -> e.map(pred).sum() }
            .sum()
    }

    fun isInside(line: Int, col: Int): Boolean {
        return line >= 0 && line < lines() && col >=0 && col < cols();
    }

    fun toString(separator: String): String {
        return data.map {
            line -> line.joinToString(separator)
        }.joinToString("\n")
    }
}
