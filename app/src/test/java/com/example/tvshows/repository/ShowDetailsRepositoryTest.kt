package com.example.tvshows.repository

import com.example.tvshows.database.dao.ShowDetailsDao
import com.example.tvshows.database.dao.ShowSummaryDao
import com.example.tvshows.database.model.ShowDetails
import com.example.tvshows.repository.service.ShowDetailsService
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Test

class ShowDetailsRepositoryTest {

    private val showDetailsService: ShowDetailsService = mock()
    private val showSummaryDao: ShowSummaryDao = mock()
    private val showDetailsDao: ShowDetailsDao = mock()
    private lateinit var showDetailsRepository: ShowDetailsRepository

    private val showId = 1
    private val showDetails = ShowDetails()

    private fun setConditions(
        showDetails: ShowDetails = this.showDetails
    ) {
        whenever(showDetailsService.getShowDetails(any())).thenReturn(Single.just(showDetails))
        whenever(showSummaryDao.getShowSummary(any())).thenReturn(Observable.never())
        whenever(showDetailsDao.getShowDetails(any())).thenReturn(Observable.never())
        whenever(showDetailsDao.insertShowDetails(any())).thenReturn(Completable.complete())
        showDetailsRepository = ShowDetailsRepository(showDetailsService, showSummaryDao, showDetailsDao)
    }

    @Test
    fun show_details_is_updated_correctly() {
        // arrange
        setConditions(showDetails)

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
        // arrange
        setConditions()

        // act
        showDetailsRepository.getShowSummary(showId = showId)
            .test()
            .dispose()

        // assert
        verify(showSummaryDao, times(1)).getShowSummary(showId)
    }

    @Test
    fun show_details_are_retrieved_correctly() {
        // arrange
        setConditions()

        // act
        showDetailsRepository.getShowDetails(showId = showId)
            .test()
            .dispose()

        // assert
        verify(showDetailsDao, times(1)).getShowDetails(showId)
    }
}