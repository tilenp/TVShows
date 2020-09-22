package com.example.tvshows.repository.paging

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tvshows.database.TVShowsDatabase
import com.example.tvshows.database.dao.PagingKeysDao
import com.example.tvshows.database.dao.ShowsDao
import com.example.tvshows.database.model.ImagePath
import com.example.tvshows.database.model.PagingKeys
import com.example.tvshows.database.model.Show
import com.example.tvshows.repository.paging.ShowsRemoteMediator.Companion.STARTING_PAGE
import com.example.tvshows.repository.service.ShowsService
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
class ShowsRemoteMediatorTest {

    private val pagingKeysDao: PagingKeysDao = mock(PagingKeysDao::class.java)
    private val showsDao: ShowsDao = mock(ShowsDao::class.java)
    private val showsService: ShowsService = mock(ShowsService::class.java)
    private val database: TVShowsDatabase = mock(TVShowsDatabase::class.java)
    private lateinit var showMediator: ShowsRemoteMediator

    private val id1 = 1L
    private val id2 = 2L
    private val id3 = 3L
    private val id4 = 4L
    private val name1 = "name1"
    private val name2 = "name2"
    private val name3 = "name3"
    private val name4 = "name4"
    private val voteAverage1 = 2F
    private val voteAverage2 = 3F
    private val voteAverage3 = 4F
    private val voteAverage4 = 5F
    private val posterPath1 = "posterPath1"
    private val posterPath2 = "posterPath2"
    private val posterPath3 = "posterPath3"
    private val posterPath4 = "posterPath4"
    private val overview1 = "overview1"
    private val overview2 = "overview2"
    private val overview3 = "overview3"
    private val overview4 = "overview4"
    val show1 = Show(
        showId = id1,
        name = name1,
        rating = voteAverage1,
        imagePath = ImagePath(posterPath1),
        summary = overview1
    )
    val show2 = Show(
        showId = id2,
        name = name2,
        rating = voteAverage2,
        imagePath = ImagePath(posterPath2),
        summary = overview2
    )
    val show3 = Show(
        showId = id3,
        name = name3,
        rating = voteAverage3,
        imagePath = ImagePath(posterPath3),
        summary = overview3
    )
    val show4 = Show(
        showId = id4,
        name = name4,
        rating = voteAverage4,
        imagePath = ImagePath(posterPath4),
        summary = overview4
    )
    val page1 = PagingSource.LoadResult.Page(listOf(show1, show2), null, 2)
    val page2 = PagingSource.LoadResult.Page(listOf(show3, show4), 1, 3)
    val pagingConfig = PagingConfig(pageSize = 40)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        `when`(database.getPagingKeysDao()).thenReturn(pagingKeysDao)
        `when`(database.getShowsDao()).thenReturn(showsDao)
        `when`(database.runInTransaction(any())).thenAnswer { (it.arguments.first() as Runnable).run() }
        showMediator = ShowsRemoteMediator(showsService = showsService, database = database)
    }

    @Test
    fun paging_keys_for_the_element_closest_to_the_current_position_are_correct() {
        // arrange
        val state: PagingState<Int, Show> = PagingState(listOf(page1, page2), 0, pagingConfig, 0)

        // act
        showMediator.getRemoteKeyForFirstItem(state = state)

        // assert
        verify(pagingKeysDao, times(1)).getPagingKeysForElementId(show1.showId)
    }

    @Test
    fun paging_keys_for_the_first_item_are_correct() {
        // arrange
        val state: PagingState<Int, Show> = PagingState(listOf(page1, page2), 0, pagingConfig, 0)

        // act
        showMediator.getRemoteKeyForFirstItem(state = state)

        // assert
        verify(pagingKeysDao, times(1)).getPagingKeysForElementId(show1.showId)
    }

    @Test
    fun paging_keys_for_the_last_item_are_correct() {
        // arrange
        val state: PagingState<Int, Show> = PagingState(listOf(page1, page2), 0, pagingConfig, 0)

        // act
        showMediator.getRemoteKeyForLastItem(state = state)

        // assert
        verify(pagingKeysDao, times(1)).getPagingKeysForElementId(show4.showId)
    }

    @Test
    fun when_load_type_is_refresh_paging_keys_are_deleted() {
        // act
        val showsWrapper = ShowsWrapper(false, emptyList())
        showMediator.updateDatabase(1, LoadType.REFRESH, showsWrapper)

        // assert
        verify(pagingKeysDao, times(1)).deletePagingKeys()
    }

    @Test
    fun when_load_type_is_refresh_shows_are_deleted() {
        // act
        val showsWrapper = ShowsWrapper(false, emptyList())
        showMediator.updateDatabase(1, LoadType.REFRESH, showsWrapper)

        // assert
        verify(showsDao, times(1)).deleteShows()
    }

    @Test
    fun when_page_is_starting_page_keys_are_stored_correctly() {
        // arrange
        val showsWrapper = ShowsWrapper(false, listOf(show1))
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
        val showsWrapper = ShowsWrapper(true, listOf(show1))
        val page = 5
        val pagingKeys = PagingKeys(
            elementId = show1.showId,
            prevKey = page - 1,
            nextKey = null
        )

        // act
        showMediator.updateDatabase(page, LoadType.REFRESH, showsWrapper)

        // assert
        verify(pagingKeysDao, times(1)).insertPagingKeys(listOf(pagingKeys))
    }

    @Test
    fun shows_are_stored_correctly() {
        // arrange
        val showsWrapper = ShowsWrapper(false, listOf(show1, show2))

        // act
        showMediator.updateDatabase(STARTING_PAGE, LoadType.REFRESH, showsWrapper)

        // assert
        verify(showsDao, times(1)).insertShows(listOf(show1, show2))
    }
}