package com.example.tvshows.ui.showlist

import android.os.Build
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.RecyclerView
import com.example.tvshows.R
import com.example.tvshows.dagger.MyTestApplication
import com.example.tvshows.dagger.TestMyViewModelFactory
import com.example.tvshows.ui.UIState
import com.example.tvshows.ui.showslist.ShowSummariesFragment
import com.example.tvshows.ui.showslist.ShowSummariesViewModel
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Flowable
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast

@ExperimentalPagingApi
@RunWith(RobolectricTestRunner::class)
@Config(application = MyTestApplication::class, sdk = [Build.VERSION_CODES.P])
class ShowSummariesFragmentTest {

    private val showSummariesViewModel: ShowSummariesViewModel = mock()

    private fun setUp(
        uiState: UIState = UIState.Default,
        message: String = "",
        navigationActionId: Int = 0
    ) {
        whenever(showSummariesViewModel.getShowSummaries()).thenReturn(Flowable.never())
        whenever(showSummariesViewModel.getUIState()).thenReturn(Observable.just(uiState))
        whenever(showSummariesViewModel.getMessage()).thenReturn(Observable.just(message))
        whenever(showSummariesViewModel.getNavigation()).thenReturn(Observable.just(navigationActionId))
        TestMyViewModelFactory.showSummariesViewModel = showSummariesViewModel
    }

    @Test
    fun loading_state_is_handled_correctly() {
        // arrange
        setUp(uiState = UIState.Loading)

        scenario().onFragment { fragment ->

            val recyclerView = fragment.view?.findViewById<RecyclerView>(R.id.shows_recycler_view)
            val progressBar = fragment.view?.findViewById<ProgressBar>(R.id.progress_bar)
            val retryButton = fragment.view?.findViewById<Button>(R.id.retry_button)

            // assert
            assertEquals(false, recyclerView?.isVisible)
            assertEquals(true, progressBar?.isVisible)
            assertEquals(false, retryButton?.isVisible)
        }
    }

    @Test
    fun retry_state_is_handled_correctly() {
        // arrange
        setUp(uiState = UIState.Retry)

        scenario().onFragment { fragment ->

            val recyclerView = fragment.view?.findViewById<RecyclerView>(R.id.shows_recycler_view)
            val progressBar = fragment.view?.findViewById<ProgressBar>(R.id.progress_bar)
            val retryButton = fragment.view?.findViewById<Button>(R.id.retry_button)

            // assert
            assertEquals(false, recyclerView?.isVisible)
            assertEquals(false, progressBar?.isVisible)
            assertEquals(true, retryButton?.isVisible)
        }
    }

    @Test
    fun success_state_is_handled_correctly() {
        // arrange
        setUp(uiState = UIState.Success)

        scenario().onFragment { fragment ->

            val recyclerView = fragment.view?.findViewById<RecyclerView>(R.id.shows_recycler_view)
            val progressBar = fragment.view?.findViewById<ProgressBar>(R.id.progress_bar)
            val retryButton = fragment.view?.findViewById<Button>(R.id.retry_button)

            // assert
            assertEquals(true, recyclerView?.isVisible)
            assertEquals(false, progressBar?.isVisible)
            assertEquals(false, retryButton?.isVisible)
        }
    }

    @Test
    fun message_is_handled_correctly() {
        // arrange
        val message = "message"
        setUp(message = message)

        scenario().onFragment { fragment ->

            // assert
            assertEquals(message, ShadowToast.getTextOfLatestToast())
        }
    }

    @Test
    fun when_show_is_clicked_view_model_is_notified() {
        // arrange
        setUp()

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