package algo

fun <T> findFirst(initial: T, isComplete: (T) -> Boolean, expand: (T) -> List<T>): T? {

    fun rec(current: T): T? {
        if (isComplete(current)) {
            return current
        }
        val nextStates = expand(current)
        for(nextState in nextStates) {
            val next = rec(nextState)
            if(next != null)
                return next
        }
        return null
    }

    return rec(initial)
}

fun <T> findAll(initial: T, isComplete: (T) -> Boolean, expand: (T) -> List<T>): List<T> {

    fun rec(current: T): List<T> {
        if (isComplete(current)) {
            return listOf(current)
        }
        val nextStates = expand(current)

        return nextStates.map { nextState -> rec(nextState) }.flatten()
    }

    return rec(initial)
}