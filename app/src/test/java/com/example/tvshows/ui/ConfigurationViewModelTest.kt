package com.example.tvshows.ui

import android.content.res.Configuration
import com.example.tvshows.ui.ConfigurationViewModel.Companion.SHOW_DETAILS_FRAGMENT
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test

class ConfigurationViewModelTest {

    private val eventAggregator: EventAggregator = mock()
    private val showSelectedSubject = PublishSubject.create<Int>()
    private val currentTagSubject = PublishSubject.create<String>()
    private lateinit var viewModel: ConfigurationViewModel

    private val showId = 1

    @Before
    fun setUp() {
        whenever(eventAggregator.observeSelectedShowId()).thenReturn(showSelectedSubject)
        whenever(eventAggregator.observeCurrentTag()).thenReturn(currentTagSubject)
        viewModel = ConfigurationViewModel(eventAggregator = eventAggregator)
    }

    @Test
    fun when_showId_is_selected_in_portrait_mode_detailsFragment_is_shown() {
        // arrange
        viewModel.initData(orientation =  Configuration.ORIENTATION_PORTRAIT, isTablet =  false)
        val showDetailsFragmentObserver =  viewModel.showDetailsObservable().test()

        // act
        showSelectedSubject.onNext(showId)

        // assert
        showDetailsFragmentObserver.assertValue(true)
    }

    @Test
    fun when_showId_is_selected_in_landscape_mode_on_phone_detailsFragment_is_shown() {
        // arrange
        viewModel.initData(orientation = Configuration.ORIENTATION_LANDSCAPE, isTablet = false)
        val showDetailsFragmentObserver =  viewModel.showDetailsObservable().test()

        // act
        showSelectedSubject.onNext(showId)

        // assert
        showDetailsFragmentObserver.assertValue(true)
    }

    @Test
    fun when_showId_is_selected_in_landscape_mode_on_tablet_detailsFragment_is_not_shown() {
        // arrange
        viewModel.initData(orientation = Configuration.ORIENTATION_LANDSCAPE, isTablet = true)
        val showDetailsFragmentObserver =  viewModel.showDetailsObservable().test()

        // act
        showSelectedSubject.onNext(showId)

        // assert
        showDetailsFragmentObserver.assertNoValues()
    }

    @Test
    fun when_detailsFragment_is_shown_in_portrait_mode_on_phone_and_orientation_changes_back_stack_is_not_popped () {
        // arrange
        viewModel.initData(orientation = Configuration.ORIENTATION_PORTRAIT, isTablet = false)
        currentTagSubject.onNext(SHOW_DETAILS_FRAGMENT)
        val popBackStackObserver =  viewModel.popBackStackObservable().test()

        // act
        viewModel.initData(orientation = Configuration.ORIENTATION_LANDSCAPE, isTablet = false)

        // assert
        popBackStackObserver.assertNoValues()
    }

    @Test
    fun when_detailsFragment_is_shown_in_portrait_mode_on_tablet_and_orientation_changes_back_stack_is_popped () {
        // arrange
        viewModel.initData(orientation = Configuration.ORIENTATION_PORTRAIT, isTablet = true)
        currentTagSubject.onNext(SHOW_DETAILS_FRAGMENT)
        val popBackStackObserver =  viewModel.popBackStackObservable().test()

        // act
        viewModel.initData(orientation = Configuration.ORIENTATION_LANDSCAPE, isTablet = true)

        // assert
        popBackStackObserver.assertValue(true)
    }

    @Test
    fun when_showSummariesFragment_is_shown_in_portrait_mode_on_tablet_and_orientation_changes_to_landscape_and_back_to_portrait_showSummariesFragment_is_shown () {
        // arrange
        viewModel.initData(orientation = Configuration.ORIENTATION_PORTRAIT, isTablet = true)
        val showDetailsFragmentObserver =  viewModel.showDetailsObservable().test()

        // act
        viewModel.initData(orientation = Configuration.ORIENTATION_LANDSCAPE, isTablet = true)
        viewModel.initData(orientation = Configuration.ORIENTATION_PORTRAIT, isTablet = true)

        // assert
        showDetailsFragmentObserver.assertNoValues()
    }

    @Test
    fun when_detailsFragment_is_shown_in_portrait_mode_on_tablet_and_orientation_changes_to_landscape_and_back_to_portrait_detailsFragment_is_shown () {
        // arrange
        viewModel.initData(orientation = Configuration.ORIENTATION_PORTRAIT, isTablet = true)
        currentTagSubject.onNext(SHOW_DETAILS_FRAGMENT)
        val showDetailsFragmentObserver =  viewModel.showDetailsObservable().test()

        // act
        viewModel.initData(orientation = Configuration.ORIENTATION_LANDSCAPE, isTablet = true)
        viewModel.initData(orientation = Configuration.ORIENTATION_PORTRAIT, isTablet = true)

        // assert
        showDetailsFragmentObserver.assertValue(true)
    }

}