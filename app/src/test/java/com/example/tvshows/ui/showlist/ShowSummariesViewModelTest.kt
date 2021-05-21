package com.example.tvshows.ui.showlist

import androidx.paging.CombinedLoadStates
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import com.example.tvshows.R
import com.example.tvshows.repository.ShowSummariesRepository
import com.example.tvshows.ui.EventAggregator
import com.example.tvshows.ui.UIState
import com.example.tvshows.ui.showslist.ShowSummariesViewModel
import com.example.tvshows.ui.showslist.ShowSummariesViewModel.Companion.MESSAGE_INTERVAL
import com.example.tvshows.utilities.ErrorParser
import com.example.tvshows.utilities.TestSchedulerProvider
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Flowable
import io.reactivex.schedulers.TestScheduler
import org.junit.Test
import java.util.concurrent.TimeUnit

@ExperimentalPagingApi
class ShowSummariesViewModelTest {

    private val showSummariesRepository: ShowSummariesRepository = mock()
    private val eventAggregator: EventAggregator = mock()
    private val testScheduler = TestScheduler()
    private val schedulerProvider = TestSchedulerProvider(testScheduler)
    private val errorParser: ErrorParser = mock()
    private val pageSize = 20
    private val maxSize = 30
    private val prefetchDistance = 5
    private val initialLoadSize = 40
    private lateinit var viewModel: ShowSummariesViewModel

    private val showId = 1
    private val error = "error"

    private fun setConditions(
        splitView: Boolean = false
    ) {
        whenever(showSummariesRepository.getShowSummaries(any())).thenReturn(Flowable.never())
        whenever(errorParser.parseError(any())).thenReturn(error)
        viewModel = ShowSummariesViewModel(
            showSummariesRepository = showSummariesRepository,
            eventAggregator = eventAggregator,
            schedulerProvider = schedulerProvider,
            errorParser = errorParser,
            pageSize = pageSize,
            maxSize = maxSize,
            prefetchDistance = prefetchDistance,
            initialLoadSize = initialLoadSize,
            splitView = splitView
        )
    }

    private fun getCombinedLoadStatesMock(
        refresh: LoadState? = null,
        prepend: LoadState? = null,
        append: LoadState? = null
    ): CombinedLoadStates {
        val combinedLoadStatesMock: CombinedLoadStates = mock()
        whenever(combinedLoadStatesMock.refresh).thenReturn(refresh)
        whenever(combinedLoadStatesMock.prepend).thenReturn(prepend)
        whenever(combinedLoadStatesMock.append).thenReturn(append)
        return combinedLoadStatesMock
    }

    @Test
    fun when_shows_are_loading_ui_state_is_correct() {
        // arrange
        val combinedLoadStates = getCombinedLoadStatesMock(
            refresh = LoadState.Loading
        )
        setConditions()
        val uiStateObservable = viewModel.getUIState()
            .test()

        // act
        viewModel.processState(combinedLoadStates, 0)

        // assert
        uiStateObservable
            .assertValue(UIState.Loading)
            .dispose()
    }

    @Test
    fun when_refresh_shows_throws_an_error_ui_state_is_correct() {
        // arrange
        val combinedLoadStates = getCombinedLoadStatesMock(
            refresh = LoadState.Error(Throwable())
        )
        setConditions()
        val uiStateObservable = viewModel.getUIState()
            .test()

        // act
        viewModel.processState(combinedLoadStates, 0)

        // assert
        uiStateObservable
            .assertValue(UIState.Retry)
            .dispose()
    }

    @Test
    fun when_refresh_shows_succeeds_ui_state_is_correct() {
        // arrange
        val combinedLoadStates = getCombinedLoadStatesMock(
            refresh = LoadState.NotLoading(false)
        )
        setConditions()
        val uiStateObservable = viewModel.getUIState()
            .test()

        // act
        viewModel.processState(combinedLoadStates, 0)

        // assert
        uiStateObservable
            .assertValue(UIState.Success)
            .dispose()
    }

    @Test
    fun when_prepend_shows_throws_an_error_ui_state_does_not_change() {
        // arrange
        val combinedLoadStates = getCombinedLoadStatesMock(
            prepend = LoadState.Error(Throwable())
        )
        setConditions()
        val uiStateObservable = viewModel.getUIState()
            .test()

        // act
        viewModel.processState(combinedLoadStates, 5)

        // assert
        uiStateObservable
            .assertNoValues()
            .dispose()
    }

    @Test
    fun when_append_shows_throws_an_error_ui_state_does_not_change() {
        // arrange
        val combinedLoadStates = getCombinedLoadStatesMock(
            append = LoadState.Error(Throwable())
        )
        setConditions()
        val uiStateObservable = viewModel.getUIState()
            .test()

        // act
        viewModel.processState(combinedLoadStates, 5)

        // assert
        uiStateObservable
            .assertNoValues()
            .dispose()
    }

    @Test
    fun when_refresh_shows_throws_an_error_client_is_notified() {
        // arrange
        val combinedLoadStates = getCombinedLoadStatesMock(
            refresh = LoadState.Error(Throwable(error))
        )
        setConditions()
        val uiStateObservable = viewModel.getMessage()
            .test()

        // act
        viewModel.processState(combinedLoadStates, 0)

        // assert
        testScheduler.advanceTimeBy(0, TimeUnit.MILLISECONDS)
        uiStateObservable
            .assertValue(error)
            .dispose()
    }

    @Test
    fun when_prepend_shows_throws_an_error_client_is_notified() {
        // arrange
        val combinedLoadStates = getCombinedLoadStatesMock(
            prepend = LoadState.Error(Throwable(error))
        )
        setConditions()
        val uiStateObservable = viewModel.getMessage()
            .test()

        // act
        viewModel.processState(combinedLoadStates, 0)

        // assert
        testScheduler.advanceTimeBy(0, TimeUnit.MILLISECONDS)
        uiStateObservable
            .assertValue(error)
            .dispose()
    }

    @Test
    fun when_append_shows_throws_an_error_client_is_notified() {
        // arrange
        val combinedLoadStates = getCombinedLoadStatesMock(
            append = LoadState.Error(Throwable(error))
        )
        setConditions()
        val uiStateObservable = viewModel.getMessage()
            .test()

        // act
        viewModel.processState(combinedLoadStates, 0)

        // assert
        testScheduler.advanceTimeBy(0, TimeUnit.MILLISECONDS)
        uiStateObservable
            .assertValue(error)
            .dispose()
    }

    @Test
    fun message_throttling_works_correctly() {
        // arrange
        val combinedLoadStates = getCombinedLoadStatesMock(
            append = LoadState.Error(Throwable(error))
        )
        setConditions()
        val uiStateObservable = viewModel.getMessage()
            .test()

        // act 1
        testScheduler.advanceTimeBy(0, TimeUnit.MILLISECONDS)
        viewModel.processState(combinedLoadStates, 5)
        viewModel.processState(combinedLoadStates, 5)

        // assert 1
        uiStateObservable
            .assertValueCount(1)
            .assertValue(error)

        // act 2
        testScheduler.advanceTimeBy(MESSAGE_INTERVAL, TimeUnit.MILLISECONDS)
        viewModel.processState(combinedLoadStates, 5)

        // assert 2
        uiStateObservable
            .assertValueCount(2)
            .assertValueSet(setOf(error, error))
            .dispose()
    }

    @Test
    fun when_show_id_is_selected_eventAggregator_is_notified() {
        // arrange
        setConditions()

        // act
        viewModel.onShowSelected(showId)

        // assert
        verify(eventAggregator, times(1)).onShowSelected(showId)
    }

    @Test
    fun when_show_id_is_selected_on_a_phone_navigation_is_executed() {
        // arrange
        setConditions(splitView = false)
        val navigationSubject = viewModel.getNavigation()
            .test()

        // act
        viewModel.onShowSelected(showId)

        // assert
        navigationSubject
            .assertValue(R.id.action_showSummariesFragment_to_showDetailsFragment)
            .dispose()
    }

    @Test
    fun when_show_id_is_selected_on_a_tabled_navigation_is_not_executed() {
        // arrange
        setConditions(splitView = true)
        val navigationSubject = viewModel.getNavigation()
            .test()

        // act
        viewModel.onShowSelected(showId)

        // assert
        navigationSubject
            .assertNoValues()
            .dispose()
    }
}