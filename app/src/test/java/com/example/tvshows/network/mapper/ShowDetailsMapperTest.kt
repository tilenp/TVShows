package com.example.tvshows.network.mapper

import com.example.tvshows.database.model.ImagePath
import com.example.tvshows.database.table.Genre
import com.example.tvshows.database.table.Season
import com.example.tvshows.database.table.ShowContent
import com.example.tvshows.network.remoteModel.RemoteGenre
import com.example.tvshows.network.remoteModel.RemoteSeason
import com.example.tvshows.network.remoteModel.RemoteShowDetails
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturnConsecutively
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Test

class ShowDetailsMapperTest {

    private val showContentMapper: ShowContentMapper = mock()
    private val genreMapper: GenreMapper = mock()
    private val seasonMapper: SeasonMapper = mock()
    private lateinit var showDetailsMapper: ShowDetailsMapper

    private val showId = 1
    private val overview = "overview"
    private val showContent = ShowContent(showId = showId, summary = overview)
    private val genreName1 = "genreName1"
    private val genreName2 = "genreName2"
    private val remoteGenre1 = RemoteGenre(genreName1)
    private val remoteGenre2 = RemoteGenre(genreName2)
    private val genre1 = Genre(id = 1, showId = showId, name = genreName1)
    private val genre2 = Genre(id = 2, showId = showId, name = genreName2)
    private val remoteGenres = listOf(remoteGenre1, remoteGenre2)
    private val genres = listOf(genre1, genre2)
    private val seasonId1 = 1
    private val seasonId2 = 2
    private val seasonName1 = "seasonName1"
    private val seasonName2 = "seasonName2"
    private val seasonNumber1 = 1
    private val seasonNumber2 = 2
    private val posterPath1 = "posterPath1"
    private val posterPath2 = "posterPath2"
    private val remoteSeason1 = RemoteSeason(id = seasonId1, name = seasonName1, seasonNumber = seasonNumber1, posterPath = posterPath1)
    private val remoteSeason2 = RemoteSeason(id = seasonId2, name = seasonName2, seasonNumber = seasonNumber2, posterPath = posterPath2)
    private val remoteSeasons = listOf(remoteSeason1, remoteSeason2)
    private val season1 = Season(id = seasonId1, showId = showId, name = seasonName1, seasonNumber = seasonNumber1, imagePath = ImagePath(posterPath1))
    private val season2 = Season(id = seasonId2, showId = showId, name = seasonName2, seasonNumber = seasonNumber2, imagePath = ImagePath(posterPath2))
    private val seasons = listOf(season1, season2)

    private fun setConditions(
        showContent: ShowContent,
        genres: List<Genre>?,
        seasons: List<Season>?
    ) {
        whenever(showContentMapper.map(any())).thenReturn(showContent)
        genres?.let { whenever(genreMapper.map(any())).doReturnConsecutively(it) }
        seasons?.let { whenever(seasonMapper.map(any())).doReturnConsecutively(it) }
        showDetailsMapper = ShowDetailsMapper(showContentMapper, genreMapper, seasonMapper)
    }

    @Test
    fun complete_remote_object_is_mapped_correctly() {
        // arrange
        val remoteShowDetails = RemoteShowDetails(id = showId, genres = remoteGenres, overview = overview, seasons = remoteSeasons)
        setConditions(
            showContent = showContent,
            genres = genres,
            seasons = seasons
        )

        // act
        val result = showDetailsMapper.map(remoteShowDetails)

        // assert
        assertEquals(showContent, result.showContent)
        assertEquals(genres, result.genres)
        assertEquals(seasons, result.seasons)
    }

    @Test
    fun incomplete_remote_object_is_mapped_correctly() {
        // arrange
        val remoteShowDetails = RemoteShowDetails(id = null, genres = null, overview = null, seasons = null)
        val emptyShowContent = ShowContent(0, "")
        setConditions(
            showContent = emptyShowContent,
            genres = null,
            seasons = null
        )

        // act
        val result = showDetailsMapper.map(remoteShowDetails)

        // assert
        assertEquals(emptyShowContent, result.showContent)
        assertEquals(emptyList<Genre>(), result.genres)
        assertEquals(emptyList<Season>(), result.seasons)
    }
}