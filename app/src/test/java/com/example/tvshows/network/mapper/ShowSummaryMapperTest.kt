package com.example.tvshows.network.mapper

import com.example.tvshows.network.remoteModel.RemoteShowSummary
import com.example.tvshows.utilities.IMAGE_URL
import com.example.tvshows.utilities.MEDIUM
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class ShowSummaryMapperTest {

    private val mapper = ShowSummaryMapper()

    private val id = 1
    private val name = "name"
    private val posterPath = "posterPath"

    @Test
    fun complete_remote_object_is_mapped_correctly() {
        // arrange
        val remoteShow = RemoteShowSummary(
            id = id,
            name = name,
            posterPath = posterPath
        )

        // act
        val result = mapper.map(remoteShow)

        // assert
        assertEquals(id, result.showId)
        assertEquals(name, result.name)
        assertEquals("$IMAGE_URL$MEDIUM$posterPath", result.imagePath?.medium)
    }

    @Test
    fun incomplete_remote_object_is_mapped_correctly() {
        // arrange
        val remoteShow = RemoteShowSummary(
            id = null,
            name = null,
            posterPath = null
        )

        // act
        val result = mapper.map(remoteShow)

        // assert
        assertEquals(0, result.showId)
        assertEquals("", result.name)
        assertEquals(null, result.imagePath?.medium)
    }
}