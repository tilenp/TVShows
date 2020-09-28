package com.example.tvshows.ui.showslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.example.tvshows.database.model.ShowSummary
import com.example.tvshows.repository.ShowSummariesRepository
import com.example.tvshows.ui.EventAggregator
import io.reactivex.Flowable
import javax.inject.Inject

class ShowSummariesViewModel @Inject constructor(
    private val showSummariesRepository: ShowSummariesRepository,
    private val eventAggregator: EventAggregator
): ViewModel() {

    fun setCurrentTag(currentTag: String?) {
        currentTag?.let { eventAggregator.setCurrentTag(it) }
    }

    fun getShowSummaries(): Flowable<PagingData<ShowSummary>> {
        return showSummariesRepository
            .getShowSummaries()
            .cachedIn(viewModelScope)
    }

    fun onShowSelected(showId: Int) {
        eventAggregator.onShowSelected(showId)
    }
}