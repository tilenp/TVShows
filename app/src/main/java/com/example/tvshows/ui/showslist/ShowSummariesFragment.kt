package com.example.tvshows.ui.showslist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.CombinedLoadStates
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tvshows.R
import com.example.tvshows.dagger.ComponentProvider
import com.example.tvshows.databinding.FragmentShowsBinding
import com.example.tvshows.ui.showslist.adapter.LoadingAdapter
import com.example.tvshows.ui.showslist.adapter.ShowSummariesAdapter
import com.example.tvshows.ui.showslist.callback.OnRetryClick
import com.example.tvshows.ui.showslist.callback.OnShowClick
import com.example.tvshows.utilities.ErrorHandler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@ExperimentalPagingApi
class ShowSummariesFragment : Fragment(), OnShowClick, OnRetryClick {

    private var _binding: FragmentShowsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var errorHandler: ErrorHandler
    private lateinit var viewModel: ShowSummariesViewModel
    private lateinit var adapter: ShowSummariesAdapter
    private val compositeDisposable = CompositeDisposable()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as ComponentProvider).provideAppComponent()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowsBinding.inflate(inflater, container, false)
        val view = binding.root
        setUpViewModel()
        setUpAdapter()
        setUpUI()
        return view
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(ShowSummariesViewModel::class.java)
        viewModel.setCurrentTag(tag)
    }

    private fun setUpAdapter() {
        adapter = ShowSummariesAdapter(this)
        adapter.addLoadStateListener { loadState ->
            handleLoadState(loadState)
            handleError(loadState)
        }
    }

    private fun handleLoadState(loadState: CombinedLoadStates) {
        val noItems = adapter.itemCount == 0
        with(binding) {
            progressBar.isVisible = loadState.refresh is LoadState.Loading && noItems
            retryButton.isVisible = loadState.refresh is LoadState.Error && noItems
        }
    }

    private fun handleError(loadState: CombinedLoadStates) {
        val errorState = loadState.refresh as? LoadState.Error
            ?: loadState.append as? LoadState.Error
            ?: loadState.prepend as? LoadState.Error
        errorState?.let { loadStateError ->
            errorHandler.handleError(loadStateError.error)
        }
    }

    private fun setUpUI() {
        with(binding) {
            val numberOfColumns = resources.getInteger(R.integer.grid_columns)
            showsRecyclerView.layoutManager = GridLayoutManager(requireContext(), numberOfColumns)
            val itemDecorator = MarginItemDecorator(requireContext().resources.getDimensionPixelSize(R.dimen.show_summary_margin_left_right))
            showsRecyclerView.addItemDecoration(itemDecorator)
            val loadingAdapter = LoadingAdapter(this@ShowSummariesFragment)
            showsRecyclerView.adapter = adapter.withLoadStateFooter(loadingAdapter)
            retryButton.setOnClickListener { adapter.retry() }
        }
    }

    override fun onStart() {
        super.onStart()
        compositeDisposable.add(
            viewModel.getShowSummaries()
                .subscribe({
                    adapter.submitData(lifecycle, it)
                }, {})
        )
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    override fun showClicked(showId: Int) {
        viewModel.onShowSelected(showId)
    }

    override fun retry() {
        adapter.retry()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}