package com.example.tvshows.ui.showdetails

import com.example.tvshows.database.model.ShowDetails
import com.example.tvshows.database.table.ShowSummary
import com.example.tvshows.repository.ShowDetailsRepository
import com.example.tvshows.ui.EventAggregator
import com.example.tvshows.utilities.TestSchedulerProvider
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.junit.Test

class ShowDetailsViewModelTest {

    private val eventAggregator: EventAggregator = mock()
    private val showDetailsRepository: ShowDetailsRepository = mock()
    private val showSelectedSubject = PublishSubject.create<Int>()
    private val showSummary: ShowSummary = mock()
    private val showDetails: ShowDetails = mock()
    private lateinit var viewModel: ShowDetailsViewModel

    private val tag = "tag"
    private val showId = 1

    private fun setConditions(
        updateShowDetailsCompletable: Completable = Completable.complete()
    ) {
        whenever(eventAggregator.observeSelectedShowId()).thenReturn(showSelectedSubject)
        whenever(showDetailsRepository.updateShowDetails(any())).thenReturn(updateShowDetailsCompletable)
        whenever(showDetailsRepository.getShowSummary(any())).thenReturn(Observable.just(showSummary))
        whenever(showDetailsRepository.getShowDetails(any())).thenReturn(Observable.just(showDetails))
        viewModel = ShowDetailsViewModel(eventAggregator = eventAggregator,  showDetailsRepository = showDetailsRepository, schedulerProvider = TestSchedulerProvider())
    }

    @Test
    fun when_updateShowDetails_throws_an_error_client_is_notified() {
        // arrange
        val error = Throwable()
        setConditions(updateShowDetailsCompletable = Completable.error(error))
        val errorObserver = viewModel.getErrors().test()

        // act
        showSelectedSubject.onNext(showId)

        // assert
        errorObserver.assertValue(error)
    }

    @Test
    fun when_currentTag_is_not_null_currentTag_is_set() {
        // arrange
        setConditions()

        // act
        viewModel.setCurrentTag(tag)

        // assert
        verify(eventAggregator, times(1)).setCurrentTag(tag)
    }

    @Test
    fun when_currentTag_is_null_currentTag_is_not_set() {
        // arrange
        setConditions()

        // act
        viewModel.setCurrentTag(null)

        // assert
        verify(eventAggregator, times(0)).setCurrentTag(any())
    }

    @Test
    fun when_showId_is_selected_client_gets_showSummary() {
        // arrange
        setConditions()
        val showSummaryObserver = viewModel.getShowSummary().test()

        // act
        showSelectedSubject.onNext(showId)

        // assert
        showSummaryObserver.assertValue(showSummary)
    }

    @Test
    fun when_showId_is_selected_client_gets_showDetails() {
        // arrange
        setConditions()
        val showDetailsObserver = viewModel.getShowDetails().test()

        // act
        showSelectedSubject.onNext(showId)

        // assert
        showDetailsObserver.assertValue(showDetails)
    }
}