package com.example.tvshows.ui.showdetails

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import com.example.tvshows.database.model.ShowDetails
import com.example.tvshows.database.table.ShowSummary
import com.example.tvshows.repository.ShowDetailsRepository
import com.example.tvshows.ui.EventAggregator
import com.example.tvshows.ui.UIState
import com.example.tvshows.utilities.ErrorParser
import com.example.tvshows.utilities.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ShowDetailsViewModel @Inject constructor(
    eventAggregator: EventAggregator,
    private val showDetailsRepository: ShowDetailsRepository,
    private val errorParser: ErrorParser,
    private val schedulerProvider: SchedulerProvider
) : ViewModel() {

    private val uiStateDispatcherSubject = PublishSubject.create<UIState>()
    private val uiStateSubject = BehaviorSubject.create<UIState>()
    private val messageSubject = PublishSubject.create<String>().toSerialized()
    private val showIdObservable = eventAggregator.observeSelectedShowId()
        .distinctUntilChanged()
    private val compositeDisposable = CompositeDisposable()

    init {
        setUpStateDispatcher()
        updateShowDetails()
    }

    private fun setUpStateDispatcher() {
        uiStateDispatcherSubject
            .distinctUntilChanged()
            .delay { uiState ->
                val delay: Long = if (uiState is UIState.Loading) 0 else LOADING_INTERVAL
                Observable.just(uiState)
                    .delay(delay, TimeUnit.MILLISECONDS, schedulerProvider.interval())
            }
            .subscribe(uiStateSubject)
    }

    private fun updateShowDetails() {
        compositeDisposable.add(
            showIdObservable
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.io())
                .doOnNext { uiStateDispatcherSubject.onNext(UIState.Loading) }
                .switchMapCompletable { showId ->
                    showDetailsRepository.updateShowDetails(showId)
                        .doOnError { error ->
                            reportError(error)
                            uiStateDispatcherSubject.onNext(UIState.Retry)
                        }
                        .onErrorResumeNext { Completable.complete() }
                }
                .subscribe({
                    System.out.println("db success")
                }, {
                    System.out.println("db error")
                })
        )
    }

    fun getUIState(): Observable<UIState> {
        return uiStateSubject
    }

    fun getShowSummary(): Observable<ShowSummary> {
        return showIdObservable
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.io())
            .switchMap { showId ->
                showDetailsRepository.getShowSummary(showId)
                    .doOnError { error -> reportError(error) }
                    .onErrorResumeNext(Observable.just(ShowSummary()))
            }
    }

    fun getShowDetails(): Observable<ShowDetails> {
        return showIdObservable
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.io())
            .switchMap { showId ->
                showDetailsRepository.getShowDetails(showId)
                    .doOnError { error -> reportError(error) }
                    .onErrorResumeNext(Observable.just(ShowDetails()))
            }
            .doOnNext { uiStateDispatcherSubject.onNext(UIState.Success) }
    }

    private fun reportError(throwable: Throwable) {
        val message = errorParser.parseError(throwable)
        messageSubject.onNext(message)
    }

    fun getMessage(): Observable<String> {
        return messageSubject
            .throttleFirst(MESSAGE_INTERVAL, TimeUnit.MILLISECONDS, schedulerProvider.interval())
    }

    fun retry() {
        compositeDisposable.clear()
        updateShowDetails()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    companion object {
        @VisibleForTesting const val LOADING_INTERVAL = 500L
        @VisibleForTesting const val MESSAGE_INTERVAL = 1000L
    }
}