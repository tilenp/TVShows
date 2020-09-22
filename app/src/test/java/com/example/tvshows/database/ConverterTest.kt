package com.example.tvshows.database

import com.example.tvshows.database.model.ImagePath
import org.junit.Assert.assertEquals
import org.junit.Test

class ConverterTest {

    private val converter = Converter()
    private val url = "url"
    private val imagePath = ImagePath(url)

    @Test
    fun imagePathToString() {
        // assert
        assertEquals(url, converter.fromImagePath(imagePath))
    }

    @Test
    fun stringToImagePath() {
        // assert
        assertEquals(imagePath, converter.toImagePath(url))
    }
}