package com.example.tvshows.ui

import android.content.res.Configuration
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ConfigurationViewModel @Inject constructor(
    private val eventAggregator: EventAggregator
) : ViewModel() {

    private val showDetailsSubject = PublishSubject.create<Boolean>()
    private val popBackStackSubject = PublishSubject.create<Boolean>()
    private var orientation: Int? = null
    private var lastPortraitTag: String? = null
    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable.add(
            eventAggregator.observeCurrentTag()
                .subscribe({currentTag ->
                    updateLastPortraitTag(currentTag)
                }, {})
        )

        compositeDisposable.add(
            eventAggregator.observeSelectedShowId()
                .subscribe({
                    setFragmentOnShowIdSelected()
                }, {})
        )
    }

    private fun updateLastPortraitTag(currentTag:String?) {
        if (isPortrait()) {
            lastPortraitTag = currentTag
        }
    }

    private fun setFragmentOnShowIdSelected() {
        showDetailsSubject.onNext(isPortrait())
    }

    fun setOrientation(orientation: Int) {
        this.orientation = orientation
        updateFragment()
    }

    private fun updateFragment() {
        showDetailsSubject.onNext(shouldShowDetails())
        popBackStackSubject.onNext(shouldPopBackStack())
    }

    private fun shouldShowDetails(): Boolean {
        return isPortrait() && lastPortraitTag == SHOW_DETAILS_FRAGMENT
    }

    private fun shouldPopBackStack(): Boolean {
        return !isPortrait() && lastPortraitTag == SHOW_DETAILS_FRAGMENT
    }

    private fun isPortrait(): Boolean {
        return orientation == Configuration.ORIENTATION_PORTRAIT
    }

    fun showDetailsObservable(): Observable<Boolean> {
        return showDetailsSubject
            .filter{show -> show}
    }

    fun popBackStackObservable(): Observable<Boolean> {
        return popBackStackSubject
            .filter{show -> show}
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    companion object {
        const val SHOW_SUMMARIES_FRAGMENT = "show_summaries_fragment"
        const val SHOW_DETAILS_FRAGMENT = "show_details_fragment"
    }
}