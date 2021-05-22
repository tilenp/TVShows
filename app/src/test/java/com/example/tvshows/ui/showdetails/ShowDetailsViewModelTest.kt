package com.example.tvshows.ui.showdetails

import com.example.tvshows.database.model.ShowDetails
import com.example.tvshows.database.table.ShowSummary
import com.example.tvshows.repository.ShowDetailsRepository
import com.example.tvshows.ui.EventAggregator
import com.example.tvshows.ui.UIState
import com.example.tvshows.utilities.ErrorParser
import com.example.tvshows.utilities.TestSchedulerProvider
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.BehaviorSubject
import org.junit.Test
import java.util.concurrent.TimeUnit

class ShowDetailsViewModelTest {

    private val eventAggregator: EventAggregator = mock()
    private val showDetailsRepository: ShowDetailsRepository = mock()
    private val errorParser: ErrorParser = mock()
    private val testScheduler = TestScheduler()
    private val schedulerProvider = TestSchedulerProvider(testScheduler)
    private val showSelectedSubject = BehaviorSubject.create<Int>()
    private val showSummary: ShowSummary = mock()
    private val showDetails: ShowDetails = mock()
    private lateinit var viewModel: ShowDetailsViewModel

    private val showId1 = 1
    private val showId2 = 2
    private val error = "error"

    private fun setConditions(
        updateShowDetailsCompletable: Completable = Completable.complete(),
        showSummaryObservable: Observable<ShowSummary> = Observable.just(showSummary),
        showDetailsObservable: Observable<ShowDetails> = Observable.just(showDetails)
    ) {
        whenever(eventAggregator.observeSelectedShowId()).thenReturn(showSelectedSubject)
        whenever(showDetailsRepository.updateShowDetails(any())).thenReturn(updateShowDetailsCompletable)
        whenever(showDetailsRepository.getShowSummary(any())).thenReturn(showSummaryObservable)
        whenever(showDetailsRepository.getShowDetails(any())).thenReturn(showDetailsObservable)
        whenever(errorParser.parseError(any())).thenReturn(error)
        viewModel = ShowDetailsViewModel(
            eventAggregator = eventAggregator,
            showDetailsRepository = showDetailsRepository,
            errorParser = errorParser,
            schedulerProvider = schedulerProvider
        )
    }

    @Test
    fun ui_states_are_dispatched_with_correct_delay() {
        // arrange
        setConditions()
        val showDetailsObservable = viewModel.getShowDetails()
            .test()
        val uiStateObservable = viewModel.getUIState()
            .test()

        // act
        showSelectedSubject.onNext(showId1)

        // assert 1
        testScheduler.advanceTimeBy(0, TimeUnit.MILLISECONDS)
        uiStateObservable
            .assertValueCount(1)
            .assertValue(UIState.Loading)

        // assert 2
        testScheduler.advanceTimeBy(ShowDetailsViewModel.LOADING_INTERVAL, TimeUnit.MILLISECONDS)
        uiStateObservable
            .assertValueCount(2)
            .assertValueAt(0, UIState.Loading)
            .assertValueAt(1, UIState.Success)

        // clean up
        showDetailsObservable.dispose()
        uiStateObservable.dispose()
    }

    @Test
    fun when_new_ui_state_is_the_same_as_the_current_ui_state_it_is_not_dispatched() {
        // arrange
        setConditions(
            showSummaryObservable = Observable.never()
        )
        val summaryObservable = viewModel.getShowSummary()
            .test()
        val uiStateObservable = viewModel.getUIState()
            .test()

        // act
        showSelectedSubject.onNext(showId1)
        showSelectedSubject.onNext(showId2)

        // assert
        testScheduler.advanceTimeBy(0, TimeUnit.MILLISECONDS)
        uiStateObservable
            .assertValueCount(1)
            .assertValue(UIState.Loading)

        // clean up
        summaryObservable.dispose()
        uiStateObservable.dispose()
    }

    @Test
    fun when_show_is_selected_loading_state_is_set() {
        // arrange
        setConditions()
        val uiStateObservable = viewModel.getUIState()
            .test()

        // act
        showSelectedSubject.onNext(showId1)

        // assert
        testScheduler.advanceTimeBy(0, TimeUnit.MILLISECONDS)
        uiStateObservable
            .assertValueCount(1)
            .assertValue(UIState.Loading)

        // clean up
        uiStateObservable.dispose()
    }

    @Test
    fun when_show_is_selected_repository_updates_database() {
        // arrange
        setConditions()

        // act
        showSelectedSubject.onNext(showId1)

        // assert
        verify(showDetailsRepository, times(1)).updateShowDetails(showId1)
    }

    @Test
    fun when_database_update_throws_an_error_retry_state_is_set() {
        // arrange
        setConditions(
            updateShowDetailsCompletable = Completable.error(Throwable(error))
        )
        val uiStateObservable = viewModel.getUIState()
            .test()

        // act
        showSelectedSubject.onNext(showId1)

        // assert
        testScheduler.advanceTimeBy(ShowDetailsViewModel.LOADING_INTERVAL, TimeUnit.MILLISECONDS)
        uiStateObservable
            .assertValueCount(2)
            .assertValueAt(0, UIState.Loading)
            .assertValueAt(1, UIState.Retry)

        // clean up
        uiStateObservable.dispose()
    }

    @Test
    fun when_database_update_throws_an_error_client_is_notified() {
        // arrange
        setConditions(
            updateShowDetailsCompletable = Completable.error(Throwable(error))
        )
        val messageSubject = viewModel.getMessage()
            .test()

        // act
        showSelectedSubject.onNext(showId1)

        // assert
        messageSubject
            .assertValue(error)

        // clean up
        messageSubject.dispose()
    }

    @Test
    fun when_database_update_throws_an_error_update_stream_is_not_terminated() {
        // arrange
        setConditions(
            updateShowDetailsCompletable = Completable.error(Throwable(error))
        )

        // act
        showSelectedSubject.onNext(showId1)
        showSelectedSubject.onNext(showId2)

        // assert
        verify(showDetailsRepository, times(1)).updateShowDetails(showId1)
        verify(showDetailsRepository, times(1)).updateShowDetails(showId2)
    }

    @Test
    fun when_the_same_show_is_selected_database_is_not_updated() {
        // arrange
        setConditions()

        // act
        showSelectedSubject.onNext(showId1)
        showSelectedSubject.onNext(showId1)

        // assert
        verify(showDetailsRepository, times(1)).updateShowDetails(showId1)
    }

    @Test
    fun when_show_id_is_selected_show_summary_is_returned() {
        // arrange
        setConditions(
            showSummaryObservable = Observable.just(showSummary)
        )
        val showSummaryObservable = viewModel.getShowSummary()
            .test()

        // act
        showSelectedSubject.onNext(showId1)

        // assert
        showSummaryObservable
            .assertValue(showSummary)
        verify(showDetailsRepository, times(1)).getShowSummary(showId1)

        // clean up
        showSummaryObservable.dispose()
    }

    @Test
    fun when_get_show_summary_throws_an_error_client_is_notified() {
        // arrange
        setConditions(
            showSummaryObservable = Observable.error(Throwable(error))
        )
        val showSummaryObservable = viewModel.getShowSummary()
            .test()
        val messageObservable = viewModel.getMessage()
            .test()

        // act
        showSelectedSubject.onNext(showId1)

        // assert
        messageObservable
            .assertValue(error)

        // clean up
        showSummaryObservable.dispose()
        messageObservable.dispose()
    }

    @Test
    fun when_database_throws_an_error_show_summary_stream_is_not_terminated() {
        // arrange
        setConditions(
            showSummaryObservable = Observable.error(Throwable(error))
        )
        val showSummaryObservable = viewModel.getShowSummary()
            .test()

        // act
        showSelectedSubject.onNext(showId1)
        showSelectedSubject.onNext(showId2)

        // assert
        verify(showDetailsRepository, times(1)).getShowSummary(showId1)
        verify(showDetailsRepository, times(1)).getShowSummary(showId2)

        // clean up
        showSummaryObservable.dispose()
    }

    @Test
    fun when_show_id_is_selected_show_details_is_returned() {
        // arrange
        setConditions(
            showDetailsObservable = Observable.just(showDetails)
        )
        val showDetailsObservable = viewModel.getShowDetails()
            .test()

        // act
        showSelectedSubject.onNext(showId1)

        // assert
        showDetailsObservable
            .assertValue(showDetails)
        verify(showDetailsRepository, times(1)).getShowDetails(showId1)

        // clean up
        showDetailsObservable.dispose()
    }

    @Test
    fun when_get_show_details_throws_an_error_client_is_notified() {
        // arrange
        setConditions(
            showDetailsObservable = Observable.error(Throwable(error))
        )
        val showDetailsObservable = viewModel.getShowDetails()
            .test()
        val messageObservable = viewModel.getMessage()
            .test()

        // act
        showSelectedSubject.onNext(showId1)

        // assert
        messageObservable
            .assertValue(error)

        // clean up
        showDetailsObservable.dispose()
        messageObservable.dispose()
    }

    @Test
    fun when_get_show_details_throws_an_error_stream_is_not_terminated() {
        // arrange
        setConditions(
            showDetailsObservable = Observable.error(Throwable(error))
        )
        val showDetailsObservable = viewModel.getShowDetails()
            .test()

        // act
        showSelectedSubject.onNext(showId1)
        showSelectedSubject.onNext(showId2)

        // assert
        verify(showDetailsRepository, times(1)).getShowDetails(showId1)
        verify(showDetailsRepository, times(1)).getShowDetails(showId2)

        // clean up
        showDetailsObservable.dispose()
    }

    @Test
    fun when_get_show_details_succeeds_success_state_is_set() {
        // arrange
        setConditions(
            showDetailsObservable = Observable.just(showDetails)
        )
        val uiStateObservable = viewModel.getUIState()
            .test()
        val showDetailsObservable = viewModel.getShowDetails()
            .test()

        // act
        showSelectedSubject.onNext(showId1)

        // assert
        testScheduler.advanceTimeBy(ShowDetailsViewModel.LOADING_INTERVAL, TimeUnit.MILLISECONDS)
        uiStateObservable
            .assertValueCount(2)
            .assertValueAt(0, UIState.Loading)
            .assertValueAt(1, UIState.Success)

        // clean up
        uiStateObservable.dispose()
        showDetailsObservable.dispose()
    }

    @Test
    fun when_retry_is_clicked_repository_updates_database() {
        // arrange
        setConditions()
        showSelectedSubject.onNext(showId1)

        // act
        viewModel.retry()

        // assert
        verify(showDetailsRepository, times(2)).updateShowDetails(showId1)
    }
}