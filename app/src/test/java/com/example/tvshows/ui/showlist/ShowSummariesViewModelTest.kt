package com.example.tvshows.ui.showlist

import com.example.tvshows.repository.ShowSummariesRepository
import com.example.tvshows.ui.EventAggregator
import com.example.tvshows.ui.showslist.ShowSummariesViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.junit.Before
import org.junit.Test

class ShowSummariesViewModelTest {

    private val showSummariesRepository: ShowSummariesRepository = mock()
    private val eventAggregator: EventAggregator = mock()
    private lateinit var viewModel: ShowSummariesViewModel

    private val tag = "tag"
    private val showId = 1

    @Before
    fun setUp() {
        viewModel = ShowSummariesViewModel(showSummariesRepository = showSummariesRepository,  eventAggregator = eventAggregator)
    }

    @Test
    fun when_currentTag_is_not_null_currentTag_is_set() {
        // act
        viewModel.setCurrentTag(tag)

        // assert
        verify(eventAggregator, times(1)).setCurrentTag(tag)
    }

    @Test
    fun when_currentTag_is_null_currentTag_is_not_set() {
        // act
        viewModel.setCurrentTag(null)

        // assert
        verifyZeroInteractions(eventAggregator)
    }

    @Test
    fun when_showId_is_selected_eventAggregator_is_notified() {
        // act
        viewModel.onShowSelected(showId)

        // assert
        verify(eventAggregator, times(1)).onShowSelected(showId)
    }
}