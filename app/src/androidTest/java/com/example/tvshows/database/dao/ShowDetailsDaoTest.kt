package com.example.tvshows.database.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.tvshows.database.TVShowsDatabase
import com.example.tvshows.database.model.ShowDetails
import com.example.tvshows.database.table.Genre
import com.example.tvshows.database.table.Season
import com.example.tvshows.database.table.ShowContent
import com.example.tvshows.database.table.ShowSummary
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShowDetailsDaoTest {

    private lateinit var database: TVShowsDatabase
    private lateinit var showDetailsDao: ShowDetailsDao

    private val showId = 1
    private val showSummary = ShowSummary(id = 1L, showId = showId, name = "name", imagePath = null)
    private val showContent = ShowContent(showId = showId, summary = "summary")
    private val genre1 = Genre(id = 1L, showId = showId, name = "name1")
    private val genre2 = Genre(id = 2L, showId = showId, name = "name2")
    private val season1 = Season(id = 3, showId = showId, name =  "name1", seasonNumber = 1, imagePath = null)
    private val season2 = Season(id = 4, showId = showId, name =  "name2", seasonNumber = 2, imagePath = null)
    private val showDetails = ShowDetails(
        showContent = showContent,
        genres = listOf(genre1, genre2),
        seasons = listOf(season1, season2)
    )

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        runBlocking {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            database = Room.inMemoryDatabaseBuilder(context, TVShowsDatabase::class.java).build()
            // insert showSummary because of the foreign key
            database.getShowSummaryDao().insertShowSummaries(listOf(showSummary))
            showDetailsDao = database.getShowDetailsDao()
            showDetailsDao.insertShowDetails(showDetails).blockingAwait()
        }
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun showDetails_is_inserted_successfully() {
        // assert
        showDetailsDao.getShowDetails(showId)
            .test()
            .assertValue(showDetails)
            .assertNoErrors()
            .dispose()
    }
}