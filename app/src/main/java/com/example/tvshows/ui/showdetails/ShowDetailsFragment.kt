package com.example.tvshows.ui.showdetails

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tvshows.dagger.ComponentProvider
import com.example.tvshows.database.table.Genre
import com.example.tvshows.database.table.Season
import com.example.tvshows.database.table.ShowContent
import com.example.tvshows.database.table.ShowSummary
import com.example.tvshows.databinding.FragmentShowDetailsBinding
import com.example.tvshows.ui.showdetails.adapter.SeasonsAdapter
import com.example.tvshows.ui.showdetails.callback.OnSeasonClick
import com.example.tvshows.utilities.ErrorHandler
import com.example.tvshows.utilities.SchedulerProvider
import com.example.tvshows.utilities.commaFormat
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ShowDetailsFragment : Fragment(), OnSeasonClick {

    private var _binding: FragmentShowDetailsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var errorHandler: ErrorHandler
    @Inject
    lateinit var schedulerProvider: SchedulerProvider
    private lateinit var viewModel: ShowDetailsViewModel
    private val compositeDisposable = CompositeDisposable()

    private lateinit var seasonsAdapter: SeasonsAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as ComponentProvider).provideAppComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        setUpViewModel()
        setUpUI()
        return view
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(ShowDetailsViewModel::class.java)
        viewModel.setCurrentTag(tag)
    }

    private fun setUpUI() {
        seasonsAdapter = SeasonsAdapter(this)
        binding.seasonsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = seasonsAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        updateUI()
    }

    private fun updateUI() {
        compositeDisposable.add(
            viewModel.getShowSummary()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.main())
                .subscribe({ showSummary ->
                    setContentVisibility()
                    updateSummaryPart(showSummary)
                }, {})
        )

        compositeDisposable.add(
            viewModel.getShowDetails()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.main())
                .subscribe({ showDetails ->
                    setContentVisibility()
                    updateGenres(showDetails.genres)
                    updateShowContent(showDetails.showContent)
                    updateSeasons(showDetails.seasons)
                }, {})
        )

        compositeDisposable.add(
            viewModel.getErrors()
                .observeOn(schedulerProvider.main())
                .subscribe({ error ->
                    errorHandler.handleError(error)
                }, {})
        )
    }

    private fun setContentVisibility() {
        with(binding) {
            selectShowTextView.isVisible = false
            showDetailsContainer.isVisible = true
        }
    }

    private fun updateSummaryPart(showSummary: ShowSummary) {
        with(binding) {
            Glide
                .with(nameTextView.context)
                .load(showSummary.imagePath?.medium)
                .into(posterImageView)
            nameTextView.text = showSummary.name
        }
    }

    private fun updateGenres(genres: List<Genre>) {
        binding.genreTextView.text = genres.commaFormat()
    }

    private fun updateShowContent(showContent: ShowContent) {
        binding.summaryTextView.text = showContent.summary
    }

    private fun updateSeasons(seasons: List<Season>) {
        binding.seasonsTextView.isVisible = seasons.isNotEmpty()
        seasonsAdapter.setSeasons(seasons)
    }

    override fun onSeasonClick(seasonName: String) {
        Toast.makeText(context, seasonName, Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}