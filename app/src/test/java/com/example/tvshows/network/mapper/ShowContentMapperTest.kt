package com.example.tvshows.network.mapper

import com.example.tvshows.network.remoteModel.RemoteShowDetails
import org.junit.Assert.assertEquals
import org.junit.Test

class ShowContentMapperTest {

    private val mapper = ShowContentMapper()

    private val showId = 1
    private val overview = "overview"

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
        assertEquals("", result.summary)
    }
}