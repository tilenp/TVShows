package com.example.tvshows.ui

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventAggregator @Inject constructor() {

    private val showSelectedSubject = BehaviorSubject.create<Int>()
    private val currentTagSubject = BehaviorSubject.create<String>()

    fun onShowSelected(showId: Int) {
        showSelectedSubject.onNext(showId)
    }

    fun observeSelectedShowId(): Observable<Int> {
        return showSelectedSubject
    }

    fun setCurrentTag(currentTag: String) {
        currentTagSubject.onNext(currentTag)
    }

    fun observeCurrentTag(): Observable<String> {
        return currentTagSubject
    }
}