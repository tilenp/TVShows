package com.example.tvshows.ui.showslist

import androidx.lifecycle.ViewModel
import com.example.tvshows.ui.EventAggregator
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ShowDetailsViewModel @Inject constructor(
    private val eventAggregator: EventAggregator
): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable.add(
            eventAggregator.observeSelectedShowId()
                .subscribe({}, {})
        )
    }

    fun setCurrentTag(currentTag: String?) {
        currentTag?.let { eventAggregator.setCurrentTag(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}