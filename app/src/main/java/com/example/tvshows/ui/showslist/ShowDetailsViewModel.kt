package com.example.tvshows.ui.showslist

import androidx.lifecycle.ViewModel
import com.example.tvshows.database.model.ShowDetails
import com.example.tvshows.database.model.ShowSummary
import com.example.tvshows.repository.ShowDetailsRepository
import com.example.tvshows.ui.EventAggregator
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ShowDetailsViewModel @Inject constructor(
    private val eventAggregator: EventAggregator,
    private val showDetailsRepository: ShowDetailsRepository
) : ViewModel() {

    private val errorSubject = PublishSubject.create<Throwable>()
    private val compositeDisposable = CompositeDisposable()

    init {
        updateShowDetails()
    }

    private fun updateShowDetails() {
        compositeDisposable.add(
            eventAggregator.observeSelectedShowId()
                .observeOn(Schedulers.io())
                .flatMapCompletable { showId ->
                    showDetailsRepository.updateShowDetails(showId)
                        .doOnError { error -> errorSubject.onNext(error) }
                        .onErrorResumeNext { Completable.complete() }
                }
                .subscribe({
                    System.out.println("db success")
                }, {
                    System.out.println("db error")
                })
        )
    }

    fun setCurrentTag(currentTag: String?) {
        currentTag?.let { eventAggregator.setCurrentTag(it) }
    }

    fun getShowSummary(): Observable<ShowSummary> {
        return eventAggregator.observeSelectedShowId()
            .switchMap { showId -> showDetailsRepository.getShowSummary(showId) }
    }

    fun getShowDetails(): Observable<ShowDetails> {
        return eventAggregator.observeSelectedShowId()
            .switchMap { showId -> showDetailsRepository.getShowDetails(showId) }
    }

    fun getErrors(): Observable<Throwable> {
        return errorSubject
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}