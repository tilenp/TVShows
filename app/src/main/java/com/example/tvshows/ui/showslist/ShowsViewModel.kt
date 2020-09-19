package com.example.tvshows.ui.showslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.example.tvshows.database.model.Show
import com.example.tvshows.repository.ShowsRepository
import io.reactivex.Flowable
import javax.inject.Inject

class ShowsViewModel @Inject constructor(
    private val showsRepository: ShowsRepository
): ViewModel() {

    fun getShows(): Flowable<PagingData<Show>> {
        return showsRepository
            .getShows()
            .cachedIn(viewModelScope)
    }
}