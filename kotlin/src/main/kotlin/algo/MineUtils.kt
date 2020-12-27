


fun List<String>.mineOne(pattern: String): List<Map<String, String>> {
    return mineAll(pattern).map { v -> v[0] }
}

fun List<String>.mineAll(pattern: String): List<List<Map<String, String>>> {
    val miner = Regex(pattern)
    val groupNames = mineGroupNames(pattern)
    return this.map { s ->
        miner
            .findAll(s)
            .map { matchResult ->
                groupNames.map { v ->
                    Pair(v, matchResult.groups[v]?.value ?: throw Exception("No group $v found in: $s"))
                }.toMap()
            }
            .toList()
    }
}

fun String.mineOne(pattern: String): Map<String, String> {
    val miner = Regex(pattern)
    val matchResult = miner.find(this) ?: return mapOf()
    return mineGroupNames(pattern)
        .map { v -> Pair(v, matchResult.groups.get(v)?.value ?: throw Exception("No group $v found in: $this")) }
        .toMap()
}

fun String.mineAll(pattern: String): List<Map<String,String>> {
    return this.mineAll(pattern, mineGroupNames(pattern))
}

fun String.mineAll(pattern: String, groupNames: Set<String>): List<Map<String, String>> {
    val miner = Regex(pattern)
    return miner
        .findAll(this)
        .map { matchResult ->
            groupNames.map { v ->
                Pair(v, matchResult.groups[v]?.value ?: throw Exception("No group $v found in: $this"))
            }.toMap()
        }
        .toList()
}

private fun mineGroupNames(pattern: String): Set<String> {
    return pattern
        .mineAll("\\(\\?\\<(?<groupName>\\w+)\\>", setOf("groupName"))
        .map { v -> v.getOrEx("groupName") }
        .toSet()
}
