package com.example.tvshows.utilities

import java.lang.StringBuilder

fun <T> List<T>.commaFormat(): String {
    val stringBuilder = StringBuilder()
    forEach { element ->
        stringBuilder.append(element)
        if (element != last()) {
            stringBuilder.append(", ")
        }
    }
    return stringBuilder.toString()
}

fun mapString(string: String?, fallback: String): String {
    return when {
        string.isNullOrBlank() -> fallback
        else -> string
    }
}