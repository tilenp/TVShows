package com.example.tvshows.network.mapper

import com.example.tvshows.network.remoteModel.RemoteGenre
import org.junit.Assert.assertEquals
import org.junit.Test

class GenreMapperTest {

    private val mapper = GenreMapper()

    private val name = "name"

    @Test
    fun complete_remote_object_is_mapped_correctly() {
        // arrange
        val remoteGenre = RemoteGenre(name = name)

        // act
        val result = mapper.map(remoteGenre)

        // assert
        assertEquals(name, result.name)
    }

    @Test
    fun incomplete_remote_object_is_mapped_correctly() {
        // arrange
        val remoteGenre = RemoteGenre(name = null)

        // act
        val result = mapper.map(remoteGenre)

        // assert
        assertEquals("", result.name)
    }
}