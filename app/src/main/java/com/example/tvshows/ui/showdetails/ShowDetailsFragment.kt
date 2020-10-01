package com.example.tvshows.ui.showslist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tvshows.dagger.MyApplication
import com.example.tvshows.database.table.Genre
import com.example.tvshows.database.table.Season
import com.example.tvshows.database.table.ShowContent
import com.example.tvshows.database.table.ShowSummary
import com.example.tvshows.databinding.FragmentShowDetailsBinding
import com.example.tvshows.ui.ErrorHandler
import com.example.tvshows.ui.showslist.callback.OnSeasonClick
import com.example.tvshows.utilities.commaFormat
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ShowDetailsFragment : Fragment(), OnSeasonClick {

    private var _binding: FragmentShowDetailsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var errorHandler: ErrorHandler
    private lateinit var viewModel: ShowDetailsViewModel
    private val compositeDisposable = CompositeDisposable()

    private lateinit var seasonsAdapter: SeasonsAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as MyApplication).appComponent.inject(this)
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ showSummary ->
                    updateSummaryPart(showSummary)
                }, {})
        )

        compositeDisposable.add(
            viewModel.getShowDetails()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ showDetails ->
                    updateGenres(showDetails.genres)
                    updateShowContent(showDetails.showContent)
                    updateSeasons(showDetails.seasons)
                }, {})
        )

        compositeDisposable.add(
            viewModel.getErrors()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ error ->
                    errorHandler.handleError(error)
                }, {})
        )
    }

    private fun updateSummaryPart(showSummary: ShowSummary) {
        with(binding) {
            Picasso
                .with(posterImageView.context)
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
        if (seasons.isNotEmpty()) {
            binding.seasonsTextView.visibility = View.VISIBLE
        }
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