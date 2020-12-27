@file:Suppress("EXPERIMENTAL_UNSIGNED_LITERALS")

import java.io.File
import java.util.*

fun main(args: Array<String>) {
    Problem21()
}

@ExperimentalUnsignedTypes
class Problem21 {

    class Recipe(val ingredients: Set<String>, val alergens: Set<String>) {
        override fun toString(): String {
            return "Recipe(i=$ingredients a=$alergens"
        }
    }

    fun parseToRecipes(lines: List<String>): List<Recipe> {
        println("Parsing ${lines.size} lines.")
        return lines.map { line ->
            val pieces = line.split("(", ")")
            val ingredients = pieces[0].split(" ").filter { s -> s.isNotBlank() }.map { s -> s.trim() }
            val alergens = pieces[1].substring(9).split(",").map { s -> s.trim() }
            Recipe(ingredients.toSet(), alergens.toSet())
        }

    }

    fun allergyFree(recipes: List<Recipe>): Set<String> {
        val allergens = recipes.flatMap { r -> r.alergens }.distinct()
        val ingredients = recipes.flatMap { r -> r.ingredients }.distinct()

        fun canValidateAllergen(ingredient: String, allergen: String): Boolean {
            return recipes.find { r -> r.alergens.contains(allergen) && !r.ingredients.contains(ingredient) } == null
        }

        val allergyFree = ingredients.filter { ingredient ->
            allergens.count { allergen -> canValidateAllergen(ingredient, allergen) } == 0
        }.toSet()

        return allergyFree
    }

    fun p1(recipes: List<Recipe>): String {
        val allergyFree = allergyFree(recipes)
        return recipes
            .flatMap { r -> r.ingredients }
            .filter { i -> allergyFree.contains(i) }
            .groupBy { s -> s }
            .mapValues { e -> e.value.count() }
            .values
            .sum()
            .toString()
    }

    fun purge(recipes: List<Recipe>, i: String, a:String): List<Recipe>? {
        var violation = false

        val purged = recipes.map { r ->
            if(r.alergens.contains(a) && !r.ingredients.contains(i)) {
                violation = true
                Recipe(setOf(), setOf())
            }else {
                val new = Recipe(r.ingredients - i, r.alergens - a)
                if(r.ingredients.size < r.alergens.size)
                    violation = true
                new
            }
        }.filter { r -> r.alergens.isNotEmpty() }

        return if(violation) null else purged
    }

    fun p2(recipes: List<Recipe>): String {

        val allergens = recipes.flatMap { r -> r.alergens }.distinct()

        fun backtrack(assumption: Map<String, String>, remaining: List<Recipe>): Map<String, String>? {
            println("assumption: $assumption, remaining: \n\t${remaining.joinToString("\n\t") }}")
            if (remaining.isEmpty()) {
                if (assumption.size == allergens.size) {
                    require(assumption.keys.toList().sorted() == allergens.sorted())
                    return assumption
                } else {
                    return null
                }
            }

            // for each assumption we try to eliminate it if possible
            val firstRecipe = remaining.first()
            for (i in firstRecipe.ingredients) {
                for (a in firstRecipe.alergens) {

                    val newRemaining = purge(remaining, i, a)

                    if(newRemaining == null)
                        continue

                    require(!assumption.containsKey(a))

                    val deeper = backtrack(
                        assumption + Pair(a, i),
                        newRemaining
                    )
                    if (deeper != null)
                        return deeper
                }
            }
            println("backtracking... assumptions: $assumption")
            return null
        }

        val allergyFreeIngredients = allergyFree(recipes)

        val sortedRecipes = recipes
            .map { r ->
                Recipe(
                    r.ingredients - allergyFreeIngredients,
                    r.alergens
                )
            }
            .sortedWith { a, b ->
                val idiff = a.ingredients.size - b.ingredients.size
                if (idiff == 0) a.alergens.size - b.alergens.size
                else idiff
            }
            .shuffled()

        val sol = backtrack(mapOf(), sortedRecipes)!!.toList().sortedBy { v -> v.first }
        println(sol.joinToString("\n"))

        var rest = recipes
        // validate the solution
        for(e in sol) {

            rest = rest.map {
                r -> require(!r.alergens.contains(e.first) || r.ingredients.contains(e.second)) { "i: ${e.second} a: ${e.first} $r" }
                Recipe(r.ingredients - e.second, r.alergens - e.first)
            }.filter { r -> r.alergens.isNotEmpty() }
        }
        require(rest.isEmpty()) { rest }

        return sol.joinToString(",") { v -> v.second }
    }

    init {
        val lines = File("../input21.txt").readLines()
        val exampleLines = File("../input21.txt.example").readLines()

        val recipes = parseToRecipes(lines)
        val exampleRecipes = parseToRecipes(exampleLines)

        val p1example = p1(exampleRecipes)
        println(p1example)

        val p1sol = p1(recipes)
        println("p1sol: ${p1sol}")

        println("p2ex: ${p2(recipes)}")


//        println("##### problem1: ${p1()}")
//        println("##### problem2: ${p2()}")
    }
}

