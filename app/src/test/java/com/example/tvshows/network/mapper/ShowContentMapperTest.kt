package com.example.tvshows.network.mapper

import android.content.Context
import com.example.tvshows.network.remoteModel.RemoteShowDetails
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ShowContentMapperTest {

    private val context: Context = mock()
    private lateinit var mapper: ShowContentMapper

    private val showId = 1
    private val overview = "overview"
    private val fallback = "fallback"

    @Before
    fun setUp() {
        whenever(context.getString(any())).thenReturn(fallback)
        mapper = ShowContentMapper(context)
    }

    @Test
    fun complete_remote_object_is_mapped_correctly() {
        // arrange
        val remoteShowDetails = RemoteShowDetails(
            id = showId,
            genres = emptyList(),
            overview = overview,
            seasons = emptyList()
        )

        // act
        val result = mapper.map(remoteShowDetails)

        // assert
        assertEquals(showId, result.showId)
        assertEquals(overview, result.summary)
    }

    @Test
    fun incomplete_remote_object_is_mapped_correctly() {
        // arrange
        val remoteShowDetails = RemoteShowDetails(
            id = null,
            genres = null,
            overview = null,
            seasons = null
        )

        // act
        val result = mapper.map(remoteShowDetails)

        // assert
        assertEquals(0, result.showId)
        assertEquals(fallback, result.summary)
    }
}