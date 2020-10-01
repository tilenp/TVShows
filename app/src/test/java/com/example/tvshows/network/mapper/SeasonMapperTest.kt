package com.example.tvshows.network.mapper

import com.example.tvshows.database.model.ImagePath
import com.example.tvshows.network.remoteModel.RemoteSeason
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SeasonMapperTest {

    private val mapper = SeasonMapper()

    private val id = 1
    private val name = "name"
    private val seasonNumber = 1
    private val posterPath = "posterPath"

    @Test
    fun complete_remote_object_is_mapped_correctly() {
        // arrange
        val remoteSeason = RemoteSeason(
            id = id,
            name = name,
            seasonNumber = seasonNumber,
            posterPath = posterPath
        )

        // act
        val result = mapper.map(remoteSeason)

        // assert
        assertEquals(id, result.id)
        assertEquals(name, result.name)
        assertEquals(seasonNumber, result.seasonNumber)
        assertEquals(ImagePath(posterPath), result.imagePath)
    }

    @Test
    fun incomplete_remote_object_is_mapped_correctly() {
        // arrange
        val remoteSeason = RemoteSeason(
            id = null,
            name = null,
            seasonNumber = null,
            posterPath = null
        )

        // act
        val result = mapper.map(remoteSeason)

        // assert
        assertEquals(0, result.id)
        assertEquals("", result.name)
        assertEquals(0, result.seasonNumber)
        assertNull(result.imagePath)
    }
}