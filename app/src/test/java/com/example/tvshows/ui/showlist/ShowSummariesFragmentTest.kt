package com.example.tvshows.ui.showlist

import android.os.Build
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
import androidx.paging.PagingData
import androidx.test.core.app.ApplicationProvider
import com.example.tvshows.dagger.DaggerTestComponent
import com.example.tvshows.dagger.MyApplication
import com.example.tvshows.dagger.TestMyViewModelFactory
import com.example.tvshows.database.table.ShowSummary
import com.example.tvshows.ui.showslist.ShowSummariesFragment
import com.example.tvshows.ui.showslist.ShowSummariesViewModel
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ShowSummariesFragmentTest {

    private val showSummariesViewModel: ShowSummariesViewModel = mock()
    private val showSummariesSubject = Flowable.never<PagingData<ShowSummary>>()

    @Before
    fun setUp() {
        setUpTestComponent()
        setUpViewModel()
    }

    private fun setUpTestComponent() {
        val myApplication = ApplicationProvider.getApplicationContext<MyApplication>()
        val testComponent = DaggerTestComponent.factory().create(myApplication)
        myApplication.appComponent = testComponent
    }

    private fun setUpViewModel() {
        whenever(showSummariesViewModel.getShowSummaries()).thenReturn(showSummariesSubject)
        TestMyViewModelFactory.showSummariesViewModel = showSummariesViewModel
    }

    @Test
    fun when_fragment_is_created_current_tag_is_set() {
        scenario().onFragment {
            // assert
            verify(showSummariesViewModel, times(1)).setCurrentTag(any())
        }
    }

    @Test
    fun when_show_is_clicked_view_model_is_notified() {
        scenario().onFragment { fragment ->
            // act
            fragment.showClicked(0)

            // assert
            verify(showSummariesViewModel, times(1)).onShowSelected(any())
        }
    }

    private fun scenario(): FragmentScenario<ShowSummariesFragment> {
        val scenario = launchFragment<ShowSummariesFragment>()
        return scenario.moveToState(Lifecycle.State.STARTED)
    }
}