package com.example.tvshows.repository.paging

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tvshows.database.TVShowsDatabase
import com.example.tvshows.database.dao.PagingKeysDao
import com.example.tvshows.database.dao.ShowSummaryDao
import com.example.tvshows.database.model.ImagePath
import com.example.tvshows.database.table.PagingKeys
import com.example.tvshows.database.table.ShowSummary
import com.example.tvshows.repository.paging.ShowSummariesRemoteMediator.Companion.INVALID_PAGE
import com.example.tvshows.repository.paging.ShowSummariesRemoteMediator.Companion.STARTING_PAGE
import com.example.tvshows.repository.service.ShowSummariesWrapperService
import com.example.tvshows.utilities.TestSchedulerProvider
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import java.io.InvalidObjectException

@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class ShowSummariesRemoteMediatorTest {

    private val pagingKeysDao: PagingKeysDao = mock(PagingKeysDao::class.java)
    private val showSummaryDao: ShowSummaryDao = mock(ShowSummaryDao::class.java)
    private val showSummariesWrapperService: ShowSummariesWrapperService = mock(ShowSummariesWrapperService::class.java)
    private val database: TVShowsDatabase = mock(TVShowsDatabase::class.java)
    private lateinit var showMediator: ShowSummariesRemoteMediator

    private val id1 = 1
    private val id2 = 2
    private val id3 = 3
    private val id4 = 4
    private val name1 = "name1"
    private val name2 = "name2"
    private val name3 = "name3"
    private val name4 = "name4"
    private val posterPath1 = "posterPath1"
    private val posterPath2 = "posterPath2"
    private val posterPath3 = "posterPath3"
    private val posterPath4 = "posterPath4"
    private val show1 = ShowSummary(
        showId = id1,
        name = name1,
        imagePath = ImagePath(posterPath1)
    )
    private val show2 = ShowSummary(
        showId = id2,
        name = name2,
        imagePath = ImagePath(posterPath2)
    )
    private val show3 = ShowSummary(
        showId = id3,
        name = name3,
        imagePath = ImagePath(posterPath3)
    )
    private val show4 = ShowSummary(
        showId = id4,
        name = name4,
        imagePath = ImagePath(posterPath4)
    )
    private val page1 = PagingSource.LoadResult.Page(listOf(show1, show2), null, 2)
    private val page2 = PagingSource.LoadResult.Page(listOf(show3, show4), 1, 3)
    private val pagingConfig = PagingConfig(pageSize = 40)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private fun setConditions(
        pagingKeysForElementId: PagingKeys? = null
    ) {
        `when`(database.getPagingKeysDao()).thenReturn(pagingKeysDao)
        `when`(database.getShowSummaryDao()).thenReturn(showSummaryDao)
        `when`(database.runInTransaction(any())).thenAnswer { (it.arguments.first() as Runnable).run() }
        `when`(pagingKeysDao.getPagingKeysForElementId(ArgumentMatchers.anyInt())).thenReturn(pagingKeysForElementId)
        showMediator = ShowSummariesRemoteMediator(showSummariesWrapperService = showSummariesWrapperService, database = database, schedulerProvider = TestSchedulerProvider())
    }

    @Test
    fun when_load_type_is_refresh_and_anchor_position_is_null_page_is_correct() {
        // arrange
        val pagingKeys = PagingKeys(elementId = 1, prevKey = null, nextKey = null)
        val state: PagingState<Int, ShowSummary> = PagingState(listOf(page1, page2), null, pagingConfig, 0)
        setConditions(pagingKeysForElementId = pagingKeys)
        // act
        val resultPage = showMediator.calculatePage(loadType = LoadType.REFRESH, state = state)

        // assert
        assertEquals(STARTING_PAGE, resultPage)
    }

    @Test
    fun when_load_type_is_refresh_and_anchor_position_is_not_null_page_is_correct() {
        // arrange
        val nextKey = 2
        val pagingKeys = PagingKeys(elementId = 1, prevKey = null, nextKey = nextKey)
        val state: PagingState<Int, ShowSummary> = PagingState(listOf(page1, page2), 1, pagingConfig, 0)
        setConditions(pagingKeysForElementId = pagingKeys)
        // act
        val resultPage = showMediator.calculatePage(loadType = LoadType.REFRESH, state = state)

        // assert
        assertEquals(1, resultPage)
    }

    @Test(expected = InvalidObjectException::class)
    fun when_load_type_is_prepend_and_pagingKeys_is_null_InvalidObjectException_is_thrown() {
        // arrange
        val state: PagingState<Int, ShowSummary> = PagingState(listOf(page1, page2), 1, pagingConfig, 0)
        setConditions(pagingKeysForElementId = null)

        // act
        showMediator.calculatePage(loadType = LoadType.PREPEND, state = state)
    }

    @Test
    fun when_load_type_is_prepend_and_pagingKeys_is_not_null_page_is_correct() {
        // arrange
        val prevKey = 1
        val nextKey = 2
        val pagingKeys = PagingKeys(elementId = 1, prevKey = prevKey, nextKey = nextKey)
        val state: PagingState<Int, ShowSummary> = PagingState(listOf(page1, page2), 1, pagingConfig, 0)
        setConditions(pagingKeysForElementId = pagingKeys)

        // act
        val resultPage = showMediator.calculatePage(loadType = LoadType.PREPEND, state = state)

        // assert
        assertEquals(prevKey, resultPage)
    }

    @Test
    fun when_load_type_is_prepend_and_prevKey_is_null_page_is_invalid() {
        // arrange
        val prevKey = null
        val nextKey = 2
        val pagingKeys = PagingKeys(elementId = 1, prevKey = prevKey, nextKey = nextKey)
        val state: PagingState<Int, ShowSummary> = PagingState(listOf(page1, page2), 1, pagingConfig, 0)
        setConditions(pagingKeysForElementId = pagingKeys)

        // act
        val resultPage = showMediator.calculatePage(loadType = LoadType.PREPEND, state = state)

        // assert
        assertEquals(INVALID_PAGE, resultPage)
    }

    @Test(expected = InvalidObjectException::class)
    fun when_load_type_is_append_and_pagingKeys_is_null_InvalidObjectException_is_thrown() {
        // arrange
        val state: PagingState<Int, ShowSummary> = PagingState(listOf(page1, page2), 1, pagingConfig, 0)
        setConditions(pagingKeysForElementId = null)

        // act
        showMediator.calculatePage(loadType = LoadType.APPEND, state = state)
    }

    @Test
    fun when_load_type_is_append_and_pagingKeys_is_not_null_page_is_correct() {
        // arrange
        val prevKey = 1
        val nextKey = 2
        val pagingKeys = PagingKeys(elementId = 1, prevKey = prevKey, nextKey = nextKey)
        val state: PagingState<Int, ShowSummary> = PagingState(listOf(page1, page2), 1, pagingConfig, 0)
        setConditions(pagingKeysForElementId = pagingKeys)

        // act
        val resultPage = showMediator.calculatePage(loadType = LoadType.APPEND, state = state)

        // assert
        assertEquals(nextKey, resultPage)
    }

    @Test
    fun when_load_type_is_append_and_nextKey_is_null_page_is_invalid() {
        // arrange
        val prevKey = 1
        val nextKey = null
        val pagingKeys = PagingKeys(elementId = 1, prevKey = prevKey, nextKey = nextKey)
        val state: PagingState<Int, ShowSummary> = PagingState(listOf(page1, page2), 1, pagingConfig, 0)
        setConditions(pagingKeysForElementId = pagingKeys)

        // act
        val resultPage = showMediator.calculatePage(loadType = LoadType.APPEND, state = state)

        // assert
        assertEquals(INVALID_PAGE, resultPage)
    }

    @Test
    fun paging_keys_for_the_closest_to_current_position_are_correct() {
        // arrange
        val state: PagingState<Int, ShowSummary> = PagingState(listOf(page1, page2), 1, pagingConfig, 0)
        setConditions()

        // act
        showMediator.getPagingKeysClosestToCurrentPosition(state = state)

        // assert
        verify(pagingKeysDao, times(1)).getPagingKeysForElementId(show2.showId)
    }

    @Test
    fun paging_keys_for_the_first_item_are_correct() {
        // arrange
        val state: PagingState<Int, ShowSummary> = PagingState(listOf(page1, page2), 0, pagingConfig, 0)
        setConditions()

        // act
        showMediator.getPagingKeysForFirstItem(state = state)

        // assert
        verify(pagingKeysDao, times(1)).getPagingKeysForElementId(show1.showId)
    }

    @Test
    fun paging_keys_for_the_last_item_are_correct() {
        // arrange
        val state: PagingState<Int, ShowSummary> = PagingState(listOf(page1, page2), 0, pagingConfig, 0)
        setConditions()

        // act
        showMediator.getPagingKeysForLastItem(state = state)

        // assert
        verify(pagingKeysDao, times(1)).getPagingKeysForElementId(show4.showId)
    }

    @Test
    fun when_load_type_is_refresh_paging_keys_are_deleted() {
        // assert
        setConditions()

        // act
        val showsWrapper = ShowSummariesWrapper(false, emptyList())
        showMediator.updateDatabase(1, LoadType.REFRESH, showsWrapper)

        // assert
        verify(pagingKeysDao, times(1)).deletePagingKeys()
    }

    @Test
    fun when_load_type_is_refresh_shows_are_deleted() {
        // assert
        setConditions()

        // act
        val showsWrapper = ShowSummariesWrapper(false, emptyList())
        showMediator.updateDatabase(1, LoadType.REFRESH, showsWrapper)

        // assert
        verify(showSummaryDao, times(1)).deleteSummaries()
    }

    @Test
    fun when_page_is_starting_page_keys_are_stored_correctly() {
        // assert
        setConditions()

        // arrange
        val showsWrapper = ShowSummariesWrapper(false, listOf(show1))
        val pagingKeys = PagingKeys(
            elementId = show1.showId,
            prevKey = null,
            nextKey = STARTING_PAGE + 1
        )

        // act
        showMediator.updateDatabase(STARTING_PAGE, LoadType.REFRESH, showsWrapper)

        // assert
        verify(pagingKeysDao, times(1)).insertPagingKeys(listOf(pagingKeys))
    }

    @Test
    fun when_endOfPagination_is_reached_keys_are_stored_correctly() {
        // arrange
        val showsWrapper = ShowSummariesWrapper(true, listOf(show1))
        val page = 5
        val pagingKeys = PagingKeys(
            elementId = show1.showId,
            prevKey = page - 1,
            nextKey = null
        )
        setConditions()

        // act
        showMediator.updateDatabase(page, LoadType.REFRESH, showsWrapper)

        // assert
        verify(pagingKeysDao, times(1)).insertPagingKeys(listOf(pagingKeys))
    }

    @Test
    fun shows_are_stored_correctly() {
        // arrange
        val showsWrapper = ShowSummariesWrapper(false, listOf(show1, show2))
        setConditions()

        // act
        showMediator.updateDatabase(STARTING_PAGE, LoadType.REFRESH, showsWrapper)

        // assert
        verify(showSummaryDao, times(1)).insertShowSummaries(listOf(show1, show2))
    }
}