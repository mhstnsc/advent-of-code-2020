typealias Bitmap = List<String>

fun Bitmap.flip(): Bitmap {
    return this.map { l ->
            l.reversed()
        }
}

fun Bitmap.width(): Int {
    return if (this.isEmpty()) 0
    else this.first().length
}

fun Bitmap.height(): Int {
    return this.size
}

fun Bitmap.rotate(angle: Int): Bitmap {
    val effectiveAngle = if(angle < 0) 360 - angle else angle

    fun Bitmap.rotateRight(): Bitmap {
        val width = this.width()
        return (0 until width)
            .map { x -> this.column(x).reversed() }
    }

    return when (effectiveAngle) {
        0 -> this
        90 -> this.rotateRight()
        180 -> this.rotateRight().rotateRight()
        270 -> this.rotateRight().rotateRight().rotateRight()
        else -> throw Exception("unknown rotation $angle")
    }
}

fun Bitmap.column(n: Int): String {
    return this.map { l -> l[n] }.joinToString("")
}

fun Bitmap.subBitmap(x: Int, y: Int, deltaw: Int, deltah: Int): Bitmap {
    return subList(y, y + height() + deltah).map { line ->
        line.substring(x, x + width() + deltaw)
    }
}

// top, right, bottom, left
fun  Bitmap.borders(): List<String> {
    return listOf(
        this.first(),
        this.column(this.size - 1),
        this.last(),
        this.column(0)
    )
}