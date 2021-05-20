package com.example.tvshows.ui.showdetails

import android.os.Build
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.example.tvshows.R
import com.example.tvshows.dagger.DaggerTestComponent
import com.example.tvshows.dagger.TVShowsApplication
import com.example.tvshows.dagger.TestMyViewModelFactory
import com.example.tvshows.database.model.ImagePath
import com.example.tvshows.database.model.ShowDetails
import com.example.tvshows.database.table.Genre
import com.example.tvshows.database.table.Season
import com.example.tvshows.database.table.ShowContent
import com.example.tvshows.database.table.ShowSummary
import com.nhaarman.mockitokotlin2.*
import io.reactivex.subjects.PublishSubject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ShowDetailsFragmentTest {

    private val showDetailsViewModel: ShowDetailsViewModel = mock()
    private val showSummarySubject = PublishSubject.create<ShowSummary>()
    private val showDetailsSubject = PublishSubject.create<ShowDetails>()
    private val errorSubject = PublishSubject.create<Throwable>()

    private val showName = "showName"
    private val showSummary = ShowSummary(id = 1, showId = 1, name = showName, imagePath = null)
    private val summary = "summary"
    private val showContent = ShowContent(1, summary)
    private val genreName1 = "genreName1"
    private val genreName2 = "genreName2"
    private val genre1 = Genre(id = 1, showId = 1, name = genreName1)
    private val genre2 = Genre(id = 2, showId = 2, name = genreName2)
    private val genres = listOf(genre1, genre2)
    private val seasonName1 = "seasonName1"
    private val seasonName2 = "seasonName2"
    private val season1 = Season(id = 1, showId = 1, name = seasonName1, seasonNumber = 1, imagePath = ImagePath("posterPath1"))
    private val season2 = Season(id = 2, showId = 2, name = seasonName2, seasonNumber = 2, imagePath = ImagePath("posterPath2"))
    private val seasons = listOf(season1, season2)
    private val showDetails = ShowDetails(showContent = showContent, genres = genres, seasons = seasons)
    private val genresString = "$genreName1, $genre2"

    @Before
    fun setUp() {
        setUpTestComponent()
        setUpViewModel()
    }

    private fun setUpTestComponent() {
        val myApplication = ApplicationProvider.getApplicationContext<TVShowsApplication>()
        val testComponent = DaggerTestComponent.factory().create(myApplication)
        myApplication.appComponent = testComponent
        testComponent.inject(this)
    }

    private fun setUpViewModel() {
        whenever(showDetailsViewModel.getShowSummary()).thenReturn(showSummarySubject)
        whenever(showDetailsViewModel.getShowDetails()).thenReturn(showDetailsSubject)
        whenever(showDetailsViewModel.getMessage()).thenReturn(errorSubject)
        TestMyViewModelFactory.showDetailsViewModel = showDetailsViewModel
    }

    @Test
    fun when_fragment_is_created_current_tag_is_set() {
        scenario().onFragment {
            // assert
            verify(showDetailsViewModel, times(1)).setCurrentTag(any())
        }
    }

    @Test
    fun when_show_is_not_selected_yet_state_is_correct() {
        scenario().onFragment {fragment ->
            // arrange
            val selectShowTextView = fragment.view?.findViewById<TextView>(R.id.select_show_textView)
            val showDetailsContainer = fragment.view?.findViewById<LinearLayout>(R.id.show_details_container)

            // assert
            assertEquals(true, selectShowTextView?.isVisible)
            assertEquals(false, showDetailsContainer?.isVisible)
        }
    }

    @Test
    fun when_showSummary_is_emitted_showSummary_is_set() {
        scenario().onFragment { fragment ->
            // arrange
            val selectShowTextView = fragment.view?.findViewById<TextView>(R.id.select_show_textView)
            val showDetailsContainer = fragment.view?.findViewById<LinearLayout>(R.id.show_details_container)
            val nameTextView = fragment.view?.findViewById<TextView>(R.id.name_text_view)

            // act
            showSummarySubject.onNext(showSummary)

            // assert
            assertEquals(false, selectShowTextView?.isVisible)
            assertEquals(true, showDetailsContainer?.isVisible)
            assertEquals(showName, nameTextView?.text?.toString())
        }
    }

    @Test
    fun when_showDetails_is_emitted_show_details_are_set() {
        scenario().onFragment { fragment ->
            // arrange
            val selectShowTextView = fragment.view?.findViewById<TextView>(R.id.select_show_textView)
            val showDetailsContainer = fragment.view?.findViewById<LinearLayout>(R.id.show_details_container)
            val genreTextView = fragment.view?.findViewById<TextView>(R.id.genre_text_view)
            val summaryTextView = fragment.view?.findViewById<TextView>(R.id.summary_text_view)
            val seasonsTextView = fragment.view?.findViewById<TextView>(R.id.seasons_text_view)
            val recyclerView = fragment.view?.findViewById<RecyclerView>(R.id.seasons_recycler_view)

            // act
            showDetailsSubject.onNext(showDetails)

            // assert
            assertEquals(false, selectShowTextView?.isVisible)
            assertEquals(true, showDetailsContainer?.isVisible)
            assertEquals(genresString, genreTextView?.text?.toString())
            assertEquals(summary, summaryTextView?.text?.toString())
            assertEquals(View.VISIBLE, seasonsTextView?.visibility)
            assertEquals(seasons.size, recyclerView?.adapter?.itemCount)
        }
    }

    @Test
    fun when_error_is_emitted_it_is_handled_correctly() {
        scenario().onFragment { fragment ->
            // act
            errorSubject.onNext(Throwable())

            // assert
            verify(errorHandler, times(1)).handleError(any())
        }
    }

    @Test
    fun when_season_is_selected_toast_is_shown() {
        scenario().onFragment { fragment ->
            // act
            fragment.onSeasonClick(seasonName1)

            // assert
            assertEquals(seasonName1, ShadowToast.getTextOfLatestToast())
        }
    }

    private fun scenario(): FragmentScenario<ShowDetailsFragment> {
        val scenario = launchFragment<ShowDetailsFragment>()
        return scenario.moveToState(Lifecycle.State.STARTED)
    }
}