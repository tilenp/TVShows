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