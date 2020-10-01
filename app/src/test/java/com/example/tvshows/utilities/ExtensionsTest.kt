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
}