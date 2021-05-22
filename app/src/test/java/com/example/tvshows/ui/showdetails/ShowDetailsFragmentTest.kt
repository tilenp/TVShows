package com.example.tvshows.ui.showdetails

import android.os.Build
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.RecyclerView
import com.example.tvshows.R
import com.example.tvshows.dagger.MyTestApplication
import com.example.tvshows.dagger.TestMyViewModelFactory
import com.example.tvshows.dagger.module.TestApplicationModule
import com.example.tvshows.database.model.ShowDetails
import com.example.tvshows.database.table.Genre
import com.example.tvshows.database.table.Season
import com.example.tvshows.database.table.ShowContent
import com.example.tvshows.database.table.ShowSummary
import com.example.tvshows.ui.UIState
import com.example.tvshows.ui.showdetails.ShowDetailsFragment.Companion.RETRY_CLICK_INTERVAL
import com.example.tvshows.utilities.TestSchedulerProvider
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(application = MyTestApplication::class, sdk = [Build.VERSION_CODES.P])
class ShowDetailsFragmentTest {

    private val testScheduler = TestScheduler()
    private val schedulerProvider = TestSchedulerProvider(testScheduler)
    private val showDetailsViewModel: ShowDetailsViewModel = mock()

    private fun setUp(
        uiState: UIState = UIState.Default,
        showSummary: ShowSummary = ShowSummary(),
        showDetails: ShowDetails = ShowDetails(),
        message: String = ""
    ) {
        whenever(showDetailsViewModel.getUIState()).thenReturn(Observable.just(uiState))
        whenever(showDetailsViewModel.getShowSummary()).thenReturn(Observable.just(showSummary))
        whenever(showDetailsViewModel.getShowDetails()).thenReturn(Observable.just(showDetails))
        whenever(showDetailsViewModel.getMessage()).thenReturn(Observable.just(message))
        TestMyViewModelFactory.showDetailsViewModel = showDetailsViewModel
        TestApplicationModule.schedulerProvider = schedulerProvider
    }

    @Test
    fun when_show_is_not_selected_yet_state_is_correct() {
        // arrange
        setUp(uiState = UIState.Default)

        scenario().onFragment { fragment ->

            val selectShowTextView = fragment.view?.findViewById<TextView>(R.id.select_show_textView)
            val progressBar = fragment.view?.findViewById<ProgressBar>(R.id.progress_bar)
            val retryButton = fragment.view?.findViewById<Button>(R.id.retry_button)
            val showDetailsContainer = fragment.view?.findViewById<LinearLayout>(R.id.show_details_container)

            // assert
            assertEquals(true, selectShowTextView?.isVisible)
            assertEquals(false, progressBar?.isVisible)
            assertEquals(false, retryButton?.isVisible)
            assertEquals(false, showDetailsContainer?.isVisible)
        }
    }

    @Test
    fun loading_state_is_handled_correctly() {
        // arrange
        setUp(uiState = UIState.Loading)

        scenario().onFragment { fragment ->

            val selectShowTextView = fragment.view?.findViewById<TextView>(R.id.select_show_textView)
            val progressBar = fragment.view?.findViewById<ProgressBar>(R.id.progress_bar)
            val retryButton = fragment.view?.findViewById<Button>(R.id.retry_button)
            val showDetailsContainer = fragment.view?.findViewById<LinearLayout>(R.id.show_details_container)

            // assert
            assertEquals(false, selectShowTextView?.isVisible)
            assertEquals(true, progressBar?.isVisible)
            assertEquals(false, retryButton?.isVisible)
            assertEquals(false, showDetailsContainer?.isVisible)
        }
    }

    @Test
    fun success_state_is_handled_correctly() {
        // arrange
        setUp(uiState = UIState.Success)

        scenario().onFragment { fragment ->

            val selectShowTextView = fragment.view?.findViewById<TextView>(R.id.select_show_textView)
            val progressBar = fragment.view?.findViewById<ProgressBar>(R.id.progress_bar)
            val retryButton = fragment.view?.findViewById<Button>(R.id.retry_button)
            val showDetailsContainer = fragment.view?.findViewById<LinearLayout>(R.id.show_details_container)

            // assert
            assertEquals(false, selectShowTextView?.isVisible)
            assertEquals(false, progressBar?.isVisible)
            assertEquals(false, retryButton?.isVisible)
            assertEquals(true, showDetailsContainer?.isVisible)
        }
    }

    @Test
    fun retry_state_is_handled_correctly() {
        // arrange
        setUp(uiState = UIState.Retry)

        scenario().onFragment { fragment ->

            val selectShowTextView = fragment.view?.findViewById<TextView>(R.id.select_show_textView)
            val progressBar = fragment.view?.findViewById<ProgressBar>(R.id.progress_bar)
            val retryButton = fragment.view?.findViewById<Button>(R.id.retry_button)
            val showDetailsContainer = fragment.view?.findViewById<LinearLayout>(R.id.show_details_container)

            // assert
            assertEquals(false, selectShowTextView?.isVisible)
            assertEquals(false, progressBar?.isVisible)
            assertEquals(true, retryButton?.isVisible)
            assertEquals(false, showDetailsContainer?.isVisible)
        }
    }

    @Test
    fun title_is_updated_correctly() {
        // arrange
        val name = "show name"
        setUp(showSummary = ShowSummary(name = name))

        scenario().onFragment { fragment ->

            val nameTextView = fragment.view?.findViewById<TextView>(R.id.name_text_view)

            // assert
            assertEquals(name, nameTextView?.text?.toString())
        }
    }

    @Test
    fun genres_are_updated_correctly() {
        // arrange
        val genreName1 = "genreName1"
        val genreName2 = "genreName2"
        val genre1 = Genre(id = 1, showId = 1, name = genreName1)
        val genre2 = Genre(id = 2, showId = 2, name = genreName2)
        setUp(showDetails = ShowDetails(genres = listOf(genre1, genre2)))

        scenario().onFragment { fragment ->

            val genreTextView = fragment.view?.findViewById<TextView>(R.id.genre_text_view)

            // assert
            assertEquals("$genreName1, $genreName2", genreTextView?.text?.toString())
        }
    }

    @Test
    fun show_content_is_updated_correctly() {
        // arrange
        val summary = "summary"
        val showContent = ShowContent(summary = summary)
        setUp(showDetails = ShowDetails(showContent = showContent))

        scenario().onFragment { fragment ->

            val summaryTextView = fragment.view?.findViewById<TextView>(R.id.summary_text_view)

            // assert
            assertEquals(summary, summaryTextView?.text?.toString())
        }
    }

    @Test
    fun seasons_are_updated_correctly() {
        // arrange
        val seasons = listOf(Season())
        setUp(showDetails = ShowDetails(seasons = listOf(Season())))

        scenario().onFragment { fragment ->

            val seasonsRecyclerView = fragment.view?.findViewById<RecyclerView>(R.id.seasons_recycler_view)

            // assert
            assertEquals(seasons.size, seasonsRecyclerView?.adapter?.itemCount)
        }
    }

    @Test
    fun when_seasons_exist_seasons_text_view_is_visible() {
        // arrange
        setUp(showDetails = ShowDetails(seasons = listOf(Season())))

        scenario().onFragment { fragment ->

            val seasonsTextView = fragment.view?.findViewById<TextView>(R.id.seasons_text_view)

            // assert
            assertEquals(true, seasonsTextView?.isVisible)
        }
    }

    @Test
    fun when_there_is_no_seasons_seasons_text_view_is_visible() {
        // arrange
        setUp(showDetails = ShowDetails(seasons = emptyList()))

        scenario().onFragment { fragment ->

            val seasonsTextView = fragment.view?.findViewById<TextView>(R.id.seasons_text_view)

            // assert
            assertEquals(false, seasonsTextView?.isVisible)
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
    fun when_season_is_selected_toast_is_shown() {
        // arrange
        val seasonName = "seasonName"
        setUp()

        scenario().onFragment { fragment ->
            // act
            fragment.onSeasonClick(seasonName)

            // assert
            assertEquals(seasonName, ShadowToast.getTextOfLatestToast())
        }
    }

    @Test
    fun when_retry_is_clicked_view_model_is_notified() {
        // arrange
        setUp(showDetails = ShowDetails(seasons = emptyList()))

        scenario().onFragment { fragment ->

            // act
            fragment.view?.findViewById<Button>(R.id.retry_button)?.performClick()

            // assert
            verify(showDetailsViewModel, times(1)).retry()
        }
    }

    @Test
    fun retry_is_click_is_throttled_correctly() {
        // arrange
        setUp(showDetails = ShowDetails(seasons = emptyList()))

        scenario().onFragment { fragment ->

            val retryButton = fragment.view?.findViewById<Button>(R.id.retry_button)

            // act 1
            retryButton?.performClick()
            retryButton?.performClick()

            // assert 1
            verify(showDetailsViewModel, times(1)).retry()

            // act 2
            testScheduler.advanceTimeBy(RETRY_CLICK_INTERVAL, TimeUnit.MILLISECONDS)
            retryButton?.performClick()

            // assert 2
            verify(showDetailsViewModel, times(2)).retry()
        }
    }

    private fun scenario(): FragmentScenario<ShowDetailsFragment> {
        val scenario = launchFragment<ShowDetailsFragment>()
        return scenario.moveToState(Lifecycle.State.STARTED)
    }
}