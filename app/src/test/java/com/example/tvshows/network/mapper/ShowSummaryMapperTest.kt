package com.example.tvshows.network.mapper

import com.example.tvshows.network.remoteModel.RemoteShowSummary
import com.example.tvshows.utilities.IMAGE_URL
import com.example.tvshows.utilities.MEDIUM
import org.junit.Assert
import org.junit.Test

class ShowSummaryMapperTest {

    private val mapper = ShowSummaryMapper()

    private val id = 1
    private val name = "name"
    private val voteAverage = 2F
    private val posterPath = "posterPath"
    private val overview = "overview"

    @Test
    fun complete_remote_object_is_mapped_correctly() {
        // arrange
        val remoteShow = RemoteShowSummary(
            id = id,
            name = name,
            voteAverage = voteAverage,
            posterPath = posterPath,
            overview = overview
        )

        // act
        val result = mapper.map(remoteShow)

        // assert
        Assert.assertEquals(id, result.showId)
        Assert.assertEquals(name, result.name)
        Assert.assertEquals(voteAverage, result.rating)
        Assert.assertEquals("$IMAGE_URL$MEDIUM$posterPath", result.imagePath?.medium)
        Assert.assertEquals(overview, result.summary)
    }

    @Test
    fun incomplete_remote_object_is_mapped_correctly() {
        // arrange
        val remoteShow = RemoteShowSummary(
            id = null,
            name = null,
            voteAverage = null,
            posterPath = null,
            overview = null
        )

        // act
        val result = mapper.map(remoteShow)

        // assert
        Assert.assertEquals(0, result.showId)
        Assert.assertEquals("", result.name)
        Assert.assertEquals(0F, result.rating)
        Assert.assertEquals(null, result.imagePath?.medium)
        Assert.assertEquals("", result.summary)
    }
}