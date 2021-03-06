package com.example.tvshows.ui.showslist

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.paging.rxjava2.cachedIn
import com.example.tvshows.R
import com.example.tvshows.database.table.ShowSummary
import com.example.tvshows.repository.ShowSummariesRepository
import com.example.tvshows.ui.EventAggregator
import com.example.tvshows.ui.UIState
import com.example.tvshows.utilities.*
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

@OptIn(ExperimentalPagingApi::class)
class ShowSummariesViewModel @Inject constructor(
    showSummariesRepository: ShowSummariesRepository,
    private val eventAggregator: EventAggregator,
    private val schedulerProvider: SchedulerProvider,
    private val errorParser: ErrorParser,
    @Named(PAGE_SIZE) private val pageSize: Int,
    @Named(MAX_SIZE) private val maxSize: Int,
    @Named(PREFETCH_DISTANCE) private val prefetchDistance: Int,
    @Named(INITIAL_LOAD_SIZE) private val initialLoadSize: Int,
    @Named(SPLIT_VIEW) private val splitView: Boolean
) : ViewModel() {

    private val pagingConfig = PagingConfig(
        pageSize = pageSize,
        enablePlaceholders = true,
        maxSize = maxSize,
        prefetchDistance = prefetchDistance,
        initialLoadSize = initialLoadSize
    )
    private val showSummariesFlowable: Flowable<PagingData<ShowSummary>> = showSummariesRepository
        .getShowSummaries(pagingConfig)
        .cachedIn(viewModelScope)
    private val uiStateSubject = BehaviorSubject.create<Pair<CombinedLoadStates, Int>>()
    private val navigationSubject = PublishSubject.create<Int>()

    fun processState(loadState: CombinedLoadStates, itemCount: Int) {
        uiStateSubject.onNext(Pair(loadState, itemCount))
    }

    fun getUIState(): Observable<UIState> {
        return uiStateSubject
            .map { data ->
                val loadState = data.first
                val noItems = data.second == 0
                when {
                    loadState.refresh is LoadState.Loading -> UIState.Loading
                    loadState.refresh is LoadState.Error && noItems -> UIState.Retry
                    loadState.refresh is LoadState.NotLoading -> UIState.Success
                    loadState.append is LoadState.NotLoading -> UIState.Success
                    loadState.prepend is LoadState.NotLoading -> UIState.Success
                    else -> UIState.Default
                }
            }
            .filter { state -> state != UIState.Default }
    }

    fun getMessage(): Observable<String> {
        return uiStateSubject
            .map { data ->
                val loadState = data.first
                when {
                    loadState.refresh is LoadState.Error -> parseError(loadState.refresh as LoadState.Error)
                    loadState.append is LoadState.Error -> parseError(loadState.append as LoadState.Error)
                    loadState.prepend is LoadState.Error -> parseError(loadState.prepend as LoadState.Error)
                    else -> ""
                }
            }
            .filter { message -> message.isNotEmpty() }
            .throttleFirst(MESSAGE_INTERVAL, TimeUnit.MILLISECONDS, schedulerProvider.interval())
    }

    private fun parseError(errorState: LoadState.Error): String {
        return errorParser.parseError(errorState.error)
    }

    fun getShowSummaries(): Flowable<PagingData<ShowSummary>> {
        return showSummariesFlowable
    }

    fun onShowSelected(showId: Int) {
        eventAggregator.onShowSelected(showId)
        if (!splitView) {
            navigationSubject.onNext(R.id.action_showSummariesFragment_to_showDetailsFragment)
        }
    }

    fun getNavigation(): Observable<Int> {
        return navigationSubject
    }

    companion object {
        @VisibleForTesting const val MESSAGE_INTERVAL = 1000L
    }
}