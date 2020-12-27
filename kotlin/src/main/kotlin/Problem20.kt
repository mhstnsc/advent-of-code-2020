@file:Suppress("EXPERIMENTAL_UNSIGNED_LITERALS")

import java.io.File

fun main(args: Array<String>) {
    Problem20()
}

typealias Image = Pair<Long, List<String>>
typealias Monster = List<Coord2D>

fun <T> List<List<T>>.add(y: Int, e: T): List<List<T>> {
    return if (y >= this.size) {
        this.plusElement(listOf(e))
    } else {
        this.mapIndexed { index, list ->
            if (index == y)
                list + e
            else
                list
        }
    }
}

val directions = listOf('N', 'E', 'S', 'W')

data class Coord2D(
    val x: Int,
    val y: Int
) {
    fun up(): Coord2D = this.copy(y = y - 1)
    fun left(): Coord2D = this.copy(x = x - 1)
    fun right(): Coord2D = this.copy(x = x + 1)
    fun down(): Coord2D = this.copy(y = y + 1)
}

data class Transformation(
    val flip: Boolean,
    val rotation: Char
) {
    override fun toString(): String {
        return "${if (flip) 1 else 0},${
            when (rotation) {
                'N' -> 0
                'E' -> 90
                'S' -> 180
                'W' -> 270
                else -> -1
            }
        }"
    }
}

data class Margin(
    val data: String,
    val image: Image,
    val transformation: Transformation,
    val direction: Char,
) {
    override fun toString(): String {
        return "Margin(data: [$data], img[${image.first}], T[$transformation], d[$direction])"
    }
}

data class ImageMargins(
    val top: Margin,
    val right: Margin,
    val bottom: Margin,
    val left: Margin
) {
    override fun toString(): String {
        return "ImageMargins(\n\t${top}\n\t${right}\n\t${bottom}\n\t${left})"
    }

    fun toList(): List<Margin> {
        return listOf(top, right, bottom, left)
    }
}

fun Image.flip(): Image {
    return Pair(
        this.first,
        this.second.map { l ->
            l.reversed()
        }
    )
}

fun Image.width(): Int {
    return if (this.second.isEmpty()) 0
    else this.second.first().length
}

fun Image.height(): Int {
    return this.second.size
}

fun Image.rotateRight(): Image {
    val width = this.second.size
    val rotatedPixels = (0 until width)
        .map { i -> this.second.map { l -> l[i] }.reversed() }
        .map { l -> l.joinToString("") }
    return Pair(this.first, rotatedPixels)
}

fun Image.rotate(direction: Char): Image {
    return when (direction) {
        'N' -> this
        'E' -> this.rotateRight()
        'S' -> this.rotateRight().rotateRight()
        'W' -> this.rotateRight().rotateRight().rotateRight()
        else -> throw Exception("unknown rotation")
    }
}

fun Image.column(n: Int): String {
    return this.second.map { l -> l[n] }.joinToString("")
}

fun Image.subImage(x: Int, y: Int, deltaw: Int, deltah: Int): Image {
    val data = this.second.subList(y, y + this.height() + deltah).map { line ->
        line.substring(x, x + width() + deltaw)
    }
    return Pair(this.first, data)
}

// top, right, bottom, left
fun Image.borders(): List<String> {
    return listOf(
        this.second.first(),
        this.column(this.second.size - 1),
        this.second.last(),
        this.column(0)
    )
}

fun Image.getMargin(t: Transformation, side: Char): Margin {
    val flipped = if (t.flip) this.flip() else this
    val rotated = flipped.rotate(t.rotation)

    return (rotated.borders() zip directions)
        .filter { p -> p.second == side }
        .map { p -> Margin(p.first, this, t, side) }
        .first()
}

fun Image.get(c: Coord2D): Char {
    return this.second[c.y][c.x]
}

private fun Image.generateAllMargins(): List<ImageMargins> {
    return listOf(false, true).map { f ->
        directions.map { r ->
            val flipped = if (f) this.flip() else this
            val rotated = flipped.rotate(r)
            val borders = rotated.borders()
            ImageMargins(
                top = Margin(borders[0], this, Transformation(f, r), 'N'),
                right = Margin(borders[1], this, Transformation(f, r), 'E'),
                bottom = Margin(borders[2], this, Transformation(f, r), 'S'),
                left = Margin(borders[3], this, Transformation(f, r), 'W')
            )
        }
    }.flatten()
}

data class ImageSnapshot(
    val i: Image,
    val t: Transformation
) {
    fun transform(): Image {
        return if (t.flip) i.flip().rotate(t.rotation)
        else i.rotate(t.rotation)
    }

    override fun toString(): String {
        return "${i.first}[${t}]"
    }
}


@ExperimentalUnsignedTypes
class Problem20 {

    fun processInput(lines: List<String>): List<Image> {
        val images = lines.split { s -> s.isBlank() }
        val imgIds = images.map { img ->
            val id = img[0].split(" ")[1].dropLast(1).toLong()
            val imgLines = img.drop(1)
            Pair(id, imgLines)
        }
        return imgIds
    }

    class State(
        val solution: List<List<ImageSnapshot>>,
        val remaining: List<Image>
    ) {
        fun width(): Int {
            return if (solution.isEmpty()) 0
            else {
                solution.first().size
            }
        }

        fun height(): Int {
            return if (solution.isEmpty()) 0
            else {
                solution.size
            }
        }

        fun isPossible(): Boolean {
            return if (width() == 0)
                true
            else {
                (solution.map { l -> l.size }.sum() + remaining.size) % width() == 0
            }
        }

        fun isComplete(): Boolean {
            return remaining.isEmpty()
        }

        fun addToSolution(coord: Coord2D, snapshot: ImageSnapshot): State {
            val newSolution = this.solution.add(coord.y, snapshot)
            return State(
                newSolution,
                remaining.filterNot { i ->
                    i.first == snapshot.i.first
                }
            )
        }

        fun getTile(coord: Coord2D): ImageSnapshot? {
            return if (coord.y < 0 || coord.y >= solution.size)
                null
            else if (coord.x < 0 || coord.x >= solution[coord.y].size)
                null
            else
                solution[coord.y][coord.x]
        }

        fun getRight(coord: Coord2D): Margin? {
            val tile = getTile(coord)
            return tile?.i?.getMargin(tile.t, 'E')
        }

        fun getBottom(coord: Coord2D): Margin? {
            val tile = getTile(coord)
            return tile?.i?.getMargin(tile.t, 'S')
        }

        override fun toString(): String {
            return "remaining: ${remaining.map { p -> p.first }}\n\t${
                solution
                    .joinToString("\n\t") { l -> l.joinToString(",") }
            }"
        }
    }

    fun p1(input: List<String>): String {
        val images = processInput(input)

        val puzzle = solvePuzzle(images)

        val corners = listOf(
            puzzle.getTile(Coord2D(0, 0))!!,
            puzzle.getTile(Coord2D(puzzle.width() - 1, 0))!!,
            puzzle.getTile(Coord2D(puzzle.width() - 1, puzzle.height() - 1))!!,
            puzzle.getTile(Coord2D(0, puzzle.height() - 1))!!
        )

        println("solution $puzzle")

        return corners.map { snap ->
            snap.i.first.toULong()
        }.fold(1uL) { acc, v ->
            acc * v
        }.toString()
    }

    fun assembleChunks(state: State): Image {
        val assembledRaster = state.solution.map { line ->
            val chunkedData = line
                .map { snapshot ->
                    snapshot
                        .transform()
                        .subImage(1, 1, -2, -2)
                }
                .map { p -> p.second }

            val height = chunkedData[0].size
            (0..height - 1).map { y ->
                chunkedData.map { img -> img[y] }.joinToString("")
            }
        }.flatten()

        return Pair(0, assembledRaster)
    }

    fun extractHashCoords(image: Image): Set<Coord2D> {
        return image.second
            .mapIndexed { y, line ->
                line.toList()
                    .mapIndexed { x, c ->
                        if (c == '#') Coord2D(x, y) else null
                    }
                    .filter { v -> v != null }
                    .map { v -> v!! }
            }.flatten().toSet()
    }

    fun dumpToFile(image: Image, name: String) {
        File("/Users/mihai/projects/advent-of-code-2020/dump20-$name.txt").printWriter().use { out ->
            out.println("Image ID: ${image.first}")
            for (line in image.second) {
                out.println("$line")
            }
        }
    }

    fun hashCoordsOfImage(image: Image): Set<Coord2D> {
        return image.second.mapIndexed { y, line ->
            line.mapIndexed { x, c ->
                if (c == '#') Coord2D(x, y) else null
            }
        }.flatten().filter { v -> v != null }.map { v -> v!! }.toSet()
    }

    fun findPattern(image: Image, pattern: Image): List<Coord2D> {
        val r = mutableListOf<Coord2D>()
        val hashOfPattern = hashCoordsOfImage(pattern)
        for (y in 0 until image.height() - pattern.height()) {
            for (x in 0 until image.width() - pattern.width()) {
                val matched = hashOfPattern.filter { c ->
                    image.second[c.y + y][c.x + x] == '#'
                }.toSet()
                if (matched == hashOfPattern) {
                    r.add(Coord2D(x, y))
                }
            }
        }
        return r
    }

    fun roughness(image: Image, t: Transformation, monsterPattern: Image) {
        val transformedImage = ImageSnapshot(image, t).transform()
        dumpToFile(transformedImage, "-${t.flip}-${t.rotation}")

        val imageHashCoords = extractHashCoords(transformedImage).toMutableSet()
        val relativeMonsterHashCoords = extractHashCoords(monsterPattern)

        val monsters = findPattern(transformedImage, monsterPattern)

        if (monsters.isNotEmpty()) {
            println("monsters found for $t -> \n\t${monsters.joinToString("\n\t")}")

            monsters.map { monster ->
                val absoluteMonsterHashCoords = relativeMonsterHashCoords.map { c ->
                    Coord2D(c.x + monster.x, c.y + monster.y)
                }
                imageHashCoords.removeAll(absoluteMonsterHashCoords)
            }

            println("Solution $t -> ${imageHashCoords.size}")
        }
    }

    fun p2(input: List<String>): String {
        val images = processInput(input)

        val puzzle = solvePuzzle(images)

        val assembledImage = assembleChunks(puzzle)

        dumpToFile(assembledImage, "assembled")

        val monsterPattern: Image = Pair(
            1, listOf(
                "                  # ",
                "#    ##    ##    ###",
                " #  #  #  #  #  #   "
            )
        )

//        roughness(assembledImage, Transformation(false,'N'), monsterPattern)

        listOf(false, true).forEach { flip ->
            directions.forEach { direction ->
                val t = Transformation(flip, direction)
                roughness(assembledImage, t, monsterPattern)
            }
        }


        return ""
    }

    fun solvePuzzle(images: List<Image>): State {
        File("/Users/mihai/projects/advent-of-code-2020/dump20.txt").printWriter().use { out ->
            for (image in images) {
                val margins = image.generateAllMargins().flatMap { m -> m.toList() }
                margins.forEach { margin ->
                    out.println("$margin")
                }
            }
        }

        var evalutionCount = 0

        fun solveFor(state: State, p: Coord2D): State? {

            if (evalutionCount++ % 100 == 0) {
                print("$evalutionCount\r")
            }

//            println("$p -> $state")

            // add special initial conditions
            val topImageMargin = state.getBottom(p.up())
            val leftImageCoord = p.left()
            val leftImageMargin = state.getRight(leftImageCoord)

            if (p.y > 0 && p.x > state.width()) {
//                println("exceeding width, dropping")
                return null
            }

            if (state.isComplete()) {
                return state
            }

            // locate the faces which might match these two in the remaining images
            for (image in state.remaining) {
                val margins = image.generateAllMargins()
                    .filter { m ->
                        (topImageMargin == null || topImageMargin.data == m.top.data) &&
                                (leftImageMargin == null || leftImageMargin.data == m.left.data)
                    }

                for (margin in margins) {
                    val nextState = state.addToSolution(p, ImageSnapshot(margin.top.image, margin.top.transformation))

                    val r: State? = if (p.y == 0) {
                        var s = solveFor(nextState, p.right())
                        if (s == null) {
                            s = solveFor(nextState, Coord2D(x = 0, y = p.y + 1))
                        }
                        s
                    } else {
                        var s: State? = null
                        if (p.x < state.width() - 1) {
                            s = solveFor(nextState, p.right())
                        } else {
                            s = solveFor(nextState, Coord2D(x = 0, y = p.y + 1))
                        }
                        s
                    }

                    if (r != null)
                        return r
                }
            }
//            println("No margin found for ${p} left: ${leftImageMargin} top: ${topImageMargin}")
            return null
        }

        val solved = solveFor(State(listOf(), images), Coord2D(0, 0))
        require(solved != null)
        return solved
    }

    init {
        // test phase
        val testImg = Image(
            2L,
            listOf("ab", "cd")
        )
        println("borders: ${testImg.borders()}")
        println("flip: ${testImg.flip()}")
        println("rotate: ${testImg.rotate('N')}")
        println("rotate: ${testImg.rotate('E')}")
        println("rotate: ${testImg.rotate('S')}")
        println("rotate: ${testImg.rotate('W')}")
        println("faces: ${testImg.generateAllMargins().joinToString("\n")}")

        println("coord left: ${Coord2D(2, 0).left()}")

        val bigInput = File("../input20.txt").readLines()
        val smallInput = File("../input20.txt.small").readLines()


        //exeu
        println("problem1: ${p1(bigInput)}")
        println("problem2: ${p2(bigInput)}")
    }
}

