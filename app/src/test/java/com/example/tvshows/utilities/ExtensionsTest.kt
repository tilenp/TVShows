package com.example.tvshows.utilities

import org.junit.Assert.assertEquals
import org.junit.Test

class ExtensionsTest {

    @Test
    fun commaFormat_works_correctly() {
        val list = listOf("element1", "element2", "element3", "element4")
        val result = "element1, element2, element3, element4"

        assertEquals(result, list.commaFormat())
    }

    @Test
    fun non_null_string_is_mapped_correctly() {
        val string = "string"
        val fallback = "fallback"

        assertEquals(string, mapString(string, fallback))
    }

    @Test
    fun null_string_is_mapped_correctly() {
        val string = null
        val fallback = "fallback"

        assertEquals(fallback, mapString(string, fallback))
    }

    @Test
    fun blank_string_is_mapped_correctly() {
        val string = " "
        val fallback = "fallback"

        assertEquals(fallback, mapString(string, fallback))
    }
}