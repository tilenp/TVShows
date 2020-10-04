package com.example.tvshows.ui.showdetails

import androidx.lifecycle.ViewModel
import com.example.tvshows.database.model.ShowDetails
import com.example.tvshows.database.table.ShowSummary
import com.example.tvshows.repository.ShowDetailsRepository
import com.example.tvshows.ui.EventAggregator
import com.example.tvshows.utilities.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ShowDetailsViewModel @Inject constructor(
    private val eventAggregator: EventAggregator,
    private val showDetailsRepository: ShowDetailsRepository,
    private val schedulerProvider: SchedulerProvider
) : ViewModel() {

    private val showSummarySubject = BehaviorSubject.createDefault(IGNORE_SHOW_SUMMARY).toSerialized()
    private val showDetailsSubject = BehaviorSubject.createDefault(IGNORE_SHOW_DETAILS).toSerialized()
    private val errorSubject = PublishSubject.create<Throwable>()
    private val compositeDisposable = CompositeDisposable()

    init {
        updateShowDetails()
        cacheLastDatabaseEmission()
    }

    private fun updateShowDetails() {
        compositeDisposable.add(
            eventAggregator.observeSelectedShowId()
                .observeOn(schedulerProvider.io())
                .doOnNext { clearCachedEmission() }
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

    private fun clearCachedEmission() {
        showSummarySubject.onNext(IGNORE_SHOW_SUMMARY)
        showDetailsSubject.onNext(IGNORE_SHOW_DETAILS)
    }

    // cache last emission because paging clears database when LoadType is REFRESH
    private fun cacheLastDatabaseEmission() {
        eventAggregator.observeSelectedShowId()
            .observeOn(schedulerProvider.io())
            .switchMap { showId -> showDetailsRepository.getShowSummary(showId) }
            .withLatestFrom(
                showSummarySubject,
                BiFunction<ShowSummary, ShowSummary, ShowSummary> { emittedShowSummary, cachedShowSummary ->
                    if (cachedShowSummary == IGNORE_SHOW_SUMMARY) emittedShowSummary else cachedShowSummary
                }
            )
            .subscribe(showSummarySubject)

        eventAggregator.observeSelectedShowId()
            .observeOn(schedulerProvider.io())
            .switchMap { showId -> showDetailsRepository.getShowDetails(showId) }
            .withLatestFrom(
                showDetailsSubject,
                BiFunction<ShowDetails, ShowDetails, ShowDetails> { emittedShowDetails, cachedShowDetails ->
                    if (cachedShowDetails == IGNORE_SHOW_DETAILS) emittedShowDetails else cachedShowDetails
                }
            )
            .subscribe(showDetailsSubject)
    }

    fun setCurrentTag(currentTag: String?) {
        currentTag?.let { eventAggregator.setCurrentTag(it) }
    }

    fun getShowSummary(): Observable<ShowSummary> {
        return showSummarySubject
            .filter { element -> element != IGNORE_SHOW_SUMMARY }
    }

    fun getShowDetails(): Observable<ShowDetails> {
        return showDetailsSubject
            .filter { element -> element != IGNORE_SHOW_DETAILS }
    }

    fun getErrors(): Observable<Throwable> {
        return errorSubject
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    companion object {
        private val IGNORE_SHOW_SUMMARY = ShowSummary()
        private val IGNORE_SHOW_DETAILS = ShowDetails()
    }
}