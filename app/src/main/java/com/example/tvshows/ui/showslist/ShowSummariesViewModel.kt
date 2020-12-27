package com.example.tvshows.ui.showslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.example.tvshows.database.table.ShowSummary
import com.example.tvshows.repository.ShowSummariesRepository
import com.example.tvshows.ui.EventAggregator
import io.reactivex.Flowable
import javax.inject.Inject

@ExperimentalPagingApi
class ShowSummariesViewModel @Inject constructor(
    private val showSummariesRepository: ShowSummariesRepository,
    private val eventAggregator: EventAggregator
): ViewModel() {

    private var showSummariesFlowable: Flowable<PagingData<ShowSummary>> = showSummariesRepository
        .getShowSummaries()
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