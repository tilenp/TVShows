package com.example.tvshows.ui.showslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.example.tvshows.database.table.ShowSummary
import com.example.tvshows.repository.ShowSummariesRepository
import com.example.tvshows.ui.EventAggregator
import com.example.tvshows.utilities.INITIAL_LOAD_SIZE
import com.example.tvshows.utilities.MAX_SIZE
import com.example.tvshows.utilities.PAGE_SIZE
import com.example.tvshows.utilities.PREFETCH_DISTANCE
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Named

@ExperimentalPagingApi
class ShowSummariesViewModel @Inject constructor(
    private val showSummariesRepository: ShowSummariesRepository,
    private val eventAggregator: EventAggregator,
    @Named(PAGE_SIZE) private val pageSize: Int,
    @Named(MAX_SIZE) private val maxSize: Int,
    @Named(PREFETCH_DISTANCE) private val prefetchDistance: Int,
    @Named(INITIAL_LOAD_SIZE) private val initialLoadSize: Int
): ViewModel() {

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

    fun getShowSummaries(): Flowable<PagingData<ShowSummary>> {
        return showSummariesFlowable
    }

    fun onShowSelected(showId: Int) {
        eventAggregator.onShowSelected(showId)
    }
}