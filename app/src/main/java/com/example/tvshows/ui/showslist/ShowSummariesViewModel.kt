package com.example.tvshows.ui.showslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.paging.rxjava2.cachedIn
import com.example.tvshows.database.table.ShowSummary
import com.example.tvshows.repository.ShowSummariesRepository
import com.example.tvshows.ui.EventAggregator
import com.example.tvshows.ui.UIState
import com.example.tvshows.utilities.*
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

@ExperimentalPagingApi
class ShowSummariesViewModel @Inject constructor(
    showSummariesRepository: ShowSummariesRepository,
    private val eventAggregator: EventAggregator,
    private val schedulerProvider: SchedulerProvider,
    private val errorParser: ErrorParser,
    @Named(PAGE_SIZE) private val pageSize: Int,
    @Named(MAX_SIZE) private val maxSize: Int,
    @Named(PREFETCH_DISTANCE) private val prefetchDistance: Int,
    @Named(INITIAL_LOAD_SIZE) private val initialLoadSize: Int,
    @Named(UI_STATE_INTERVAL) private val uiStateInterval: Long
) : ViewModel() {

    private val uiStateSubject = BehaviorSubject.create<Pair<CombinedLoadStates, Int>>()
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

    fun setCurrentTag(currentTag: String?) {
        currentTag?.let { eventAggregator.setCurrentTag(it) }
    }

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

    fun getMessage(): Observable<Any> {
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
            .filter { message -> (message as? String)?.isNotEmpty() ?: true }
            .throttleFirst(uiStateInterval, TimeUnit.MILLISECONDS, schedulerProvider.interval())
    }

    private fun parseError(errorState: LoadState.Error): Any {
        return errorParser.parseError(errorState.error)
    }

    fun getShowSummaries(): Flowable<PagingData<ShowSummary>> {
        return showSummariesFlowable
    }

    fun onShowSelected(showId: Int) {
        eventAggregator.onShowSelected(showId)
    }
}