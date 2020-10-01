package com.example.tvshows.database.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.tvshows.database.TVShowsDatabase
import com.example.tvshows.database.table.ShowSummary
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShowSummaryDaoTest {

    private lateinit var database: TVShowsDatabase
    private lateinit var showSummaryDao: ShowSummaryDao

    private val showId1 = 1
    private val showId2 = 2
    private val showSummary1 = ShowSummary(id = 1L, showId = showId1, name = "name", imagePath = null)
    private val showSummary2 = ShowSummary(id = 2L, showId = showId2, name = "name", imagePath = null)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, TVShowsDatabase::class.java).build()
        showSummaryDao = database.getShowSummaryDao()
        showSummaryDao.insertShowSummaries(listOf(showSummary1, showSummary2))
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun showSummaries_are_inserted_successfully() {
        // assert
        showSummaryDao.getShowSummary(showId1)
            .test()
            .assertValue(showSummary1)
            .assertNoErrors()
            .dispose()
    }
}