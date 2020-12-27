import java.lang.Exception

fun <K,V> Map<K,V>.getOrEx(k: K): V {
    return this[k]?: throw Exception("Key $k is not found")
}