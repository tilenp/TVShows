package com.example.tvshows.ui.showslist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tvshows.dagger.MyApplication
import com.example.tvshows.database.model.ShowDetails
import com.example.tvshows.database.model.ShowSummary
import com.example.tvshows.databinding.FragmentShowDetailsBinding
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ShowDetailsFragment : Fragment() {

    private var _binding: FragmentShowDetailsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ShowDetailsViewModel
    private val compositeDisposable = CompositeDisposable()

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
                    updateDetailsPart(showDetails)
                }, {
                    System.out.println("db error")
                })
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

    private fun updateDetailsPart(showDetails: ShowDetails) {
        with(binding) {
            summaryTextView.text = showDetails.summary
        }
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