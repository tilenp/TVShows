package com.example.tvshows.ui.showlist

import androidx.paging.ExperimentalPagingApi
import com.example.tvshows.repository.ShowSummariesRepository
import com.example.tvshows.ui.EventAggregator
import com.example.tvshows.ui.showslist.ShowSummariesViewModel
import com.example.tvshows.utilities.ErrorParser
import com.example.tvshows.utilities.TestSchedulerProvider
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test

@ExperimentalPagingApi
class ShowSummariesViewModelTest {

    private val showSummariesRepository: ShowSummariesRepository = mock()
    private val eventAggregator: EventAggregator = mock()
    private val errorParser: ErrorParser = mock()
    private val pageSize = 1
    private val maxSize = 2
    private val prefetchDistance = 3
    private val initialLoadSize = 4
    private val uiStateInterval = 5L
    private val splitView = true
    private lateinit var viewModel: ShowSummariesViewModel

    private val showId = 1

    @Before
    fun setUp() {
        viewModel = ShowSummariesViewModel(
            showSummariesRepository = showSummariesRepository,
            eventAggregator = eventAggregator,
            schedulerProvider = TestSchedulerProvider(),
            errorParser = errorParser,
            pageSize = pageSize,
            maxSize = maxSize,
            prefetchDistance = prefetchDistance,
            initialLoadSize = initialLoadSize,
            uiStateInterval = uiStateInterval,
            splitView = splitView
        )
    }

    @Test
    fun when_showId_is_selected_eventAggregator_is_notified() {
        // act
        viewModel.onShowSelected(showId)

        // assert
        verify(eventAggregator, times(1)).onShowSelected(showId)
    }
}