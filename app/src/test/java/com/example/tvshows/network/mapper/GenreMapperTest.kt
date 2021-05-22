package com.example.tvshows.network.mapper

import android.content.Context
import com.example.tvshows.network.remoteModel.RemoteGenre
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GenreMapperTest {

    private val context: Context = mock()
    private lateinit var mapper: GenreMapper

    private val name = "name"
    private val fallback = "fallback"

    @Before
    fun setUp() {
        whenever(context.getString(any())).thenReturn(fallback)
        mapper = GenreMapper(context)
    }

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
        assertEquals(fallback, result.name)
    }
}