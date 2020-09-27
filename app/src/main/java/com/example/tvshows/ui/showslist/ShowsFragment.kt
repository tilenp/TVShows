package com.example.tvshows.ui.showslist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tvshows.R
import com.example.tvshows.dagger.MyApplication
import com.example.tvshows.databinding.FragmentShowsBinding
import com.example.tvshows.ui.ErrorHandler
import com.example.tvshows.ui.showslist.adapter.LoadingAdapter
import com.example.tvshows.ui.showslist.callback.OnShowClick
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ShowsFragment : Fragment(), OnShowClick {

    private var _binding: FragmentShowsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var errorHandler: ErrorHandler
    private lateinit var viewModel: ShowsViewModel
    private lateinit var adapter: ShowsAdapter
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
        _binding = FragmentShowsBinding.inflate(inflater, container, false)
        val view = binding.root
        setUpViewModel()
        setUpRecyclerView()
        return view
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(ShowsViewModel::class.java)
    }

    private fun setUpRecyclerView() {
        adapter = ShowsAdapter(this)
        with(binding) {
            val numberOfColumns = resources.getInteger(R.integer.grid_columns)
            showsRecyclerView.layoutManager = GridLayoutManager(requireContext(), numberOfColumns)
            showsRecyclerView.adapter = adapter.withLoadStateFooter(
                footer = LoadingAdapter()
            )

            adapter.addLoadStateListener { loadState ->
                val itemsAvailable = adapter.itemCount > 0
                showsRecyclerView.isVisible = itemsAvailable
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading && !itemsAvailable
                val errorState = loadState.mediator?.refresh as? LoadState.Error
                    ?: loadState.mediator?.append as? LoadState.Error
                    ?: loadState.mediator?.prepend as? LoadState.Error
                errorState?.let { loadStateError ->
                    errorHandler.handleError(loadStateError.error)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        compositeDisposable.add(
            viewModel.getShows()
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
        Toast.makeText(requireContext(), showId.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}