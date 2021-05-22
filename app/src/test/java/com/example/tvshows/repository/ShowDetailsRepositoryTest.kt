package com.example.tvshows.repository

import com.example.tvshows.database.dao.ShowDetailsDao
import com.example.tvshows.database.dao.ShowSummaryDao
import com.example.tvshows.database.model.ShowDetails
import com.example.tvshows.database.table.Season
import com.example.tvshows.database.table.ShowSummary
import com.example.tvshows.repository.service.ShowDetailsService
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class ShowDetailsRepositoryTest {

    private val showDetailsService: ShowDetailsService = mock()
    private val showSummaryDao: ShowSummaryDao = mock()
    private val showDetailsDao: ShowDetailsDao = mock()
    private val seasonNumberComparator: Comparator<Season?> = mock()
    private lateinit var showDetailsRepository: ShowDetailsRepository

    private val showId = 1
    private val seasons = listOf(Season(), Season())
    private val showDetails = ShowDetails(seasons = seasons)
    private val showSummary = ShowSummary()

    @Before
    fun setUp() {
        whenever(showDetailsService.getShowDetails(any())).thenReturn(Single.just(showDetails))
        whenever(showSummaryDao.getShowSummary(any())).thenReturn(Observable.just(showSummary))
        whenever(showDetailsDao.getShowDetails(any())).thenReturn(Observable.just(showDetails))
        whenever(showDetailsDao.insertShowDetails(any())).thenReturn(Completable.complete())
        whenever(seasonNumberComparator.compare(any(), any())).thenReturn(1)
        showDetailsRepository = ShowDetailsRepository(showDetailsService, showSummaryDao, showDetailsDao, seasonNumberComparator)
    }

    @Test
    fun show_details_is_updated_correctly() {
        // act
        showDetailsRepository.updateShowDetails(showId = showId)
            .test()
            .assertComplete()
            .dispose()

        // assert
        verify(showDetailsService, times(1)).getShowDetails(showId)
        verify(showDetailsDao, times(1)).insertShowDetails(showDetails)
    }

    @Test
    fun show_summary_is_retrieved_correctly() {
        // act
        showDetailsRepository.getShowSummary(showId = showId)
            .test()
            .assertValue(showSummary)
            .dispose()

        // assert
        verify(showSummaryDao, times(1)).getShowSummary(showId)
    }

    @Test
    fun show_details_are_retrieved_correctly() {
        // act
        showDetailsRepository.getShowDetails(showId = showId)
            .test()
            .assertValue(showDetails)
            .dispose()

        // assert
        verify(showDetailsDao, times(1)).getShowDetails(showId)
    }

    @Test
    fun show_seasons_are_ordered_by_season_number() {
        // act
        showDetailsRepository.getShowDetails(showId = showId)
            .test()
            .dispose()

        // assert
        verify(seasonNumberComparator, atLeastOnce()).compare(any(), any())
    }
}