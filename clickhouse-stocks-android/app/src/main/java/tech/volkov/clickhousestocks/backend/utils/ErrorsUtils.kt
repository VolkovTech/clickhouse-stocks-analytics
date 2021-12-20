package tech.volkov.clickhousestocks.backend.utils

fun <T> getOrNull(block: () -> T): T? {
    return try {
        block()
    } catch (ex: Exception) {
        println(ex)
        null
    }
}