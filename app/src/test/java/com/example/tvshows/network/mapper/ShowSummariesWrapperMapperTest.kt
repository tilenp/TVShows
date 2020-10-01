package com.example.tvshows.network.mapper

import com.example.tvshows.database.model.ImagePath
import com.example.tvshows.database.table.ShowSummary
import com.example.tvshows.network.remoteModel.RemoteShowSummary
import com.example.tvshows.network.remoteModel.RemoteWrapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturnConsecutively
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Test

class ShowSummariesWrapperMapperTest {

    private val showSummaryMapper: ShowSummaryMapper = mock()
    private lateinit var wrapperMapper: ShowSummariesWrapperMapper

    private val id1 = 1
    private val id2 = 2
    private val name1 = "name1"
    private val name2 = "name2"
    private val posterPath1 = "posterPath1"
    private val posterPath2 = "posterPath2"

    private fun setConditions(
        showSummaries: List<ShowSummary>
    ) {
        whenever(showSummaryMapper.map(any())).doReturnConsecutively(showSummaries)
        wrapperMapper = ShowSummariesWrapperMapper(showSummaryMapper)
    }

    @Test
    fun remote_object_is_mapped_correctly() {
        // arrange
        val shows = getShows()
        setConditions(showSummaries = shows)

        // act
        val totalPages = 2
        val page = 1
        val remoteWrapper = RemoteWrapper(
            totalPages = totalPages,
            page = page,
            results = getRemoteShows()
        )
        val result = wrapperMapper.map(remoteWrapper)

        Assert.assertFalse(result.endOfPaginationReached)
        result.showSummaries.forEachIndexed { index, show ->
            Assert.assertEquals(show, result.showSummaries[index])
        }
    }

    private fun getShows(): List<ShowSummary> {
        val show1 = ShowSummary(
            showId = id1,
            name = name1,
            imagePath = ImagePath(posterPath1)
        )
        val show2 = ShowSummary(
            showId = id2,
            name = name2,
            imagePath = ImagePath(posterPath2)
        )
        return listOf(show1, show2)
    }

    private fun getRemoteShows(): List<RemoteShowSummary> {
        val remoteShow1 = RemoteShowSummary(
            id = id1,
            name = name1,
            posterPath = posterPath1
        )
        val remoteShow2 = RemoteShowSummary(
            id = id2,
            name = name2,
            posterPath = posterPath2
        )
        return listOf(remoteShow1, remoteShow2)
    }
}