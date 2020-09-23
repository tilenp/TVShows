package com.example.tvshows.ui.showslist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tvshows.R
import com.example.tvshows.dagger.MyApplication
import com.example.tvshows.databinding.ActivityMainBinding
import com.example.tvshows.ui.ErrorHandler
import com.example.tvshows.ui.showslist.adapter.LoadingAdapter
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var errorHandler: ErrorHandler
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ShowsViewModel
    private val adapter = ShowsAdapter()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as MyApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setUpViewModel()
        setUpRecyclerView()
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(ShowsViewModel::class.java)
    }

    private fun setUpRecyclerView() {
        val numberOfColumns = resources.getInteger(R.integer.grid_columns)
        binding.showsRecyclerView.layoutManager = GridLayoutManager(this, numberOfColumns)
        binding.showsRecyclerView.adapter = adapter.withLoadStateFooter(
            footer = LoadingAdapter()
        )

        adapter.addLoadStateListener { loadState ->
            val itemsAvailable = adapter.itemCount > 0
            binding.showsRecyclerView.isVisible = itemsAvailable
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading && !itemsAvailable
            val errorState = loadState.mediator?.refresh as? LoadState.Error
                ?: loadState.mediator?.append as? LoadState.Error
                ?: loadState.mediator?.prepend as? LoadState.Error
            errorState?.let {
                errorHandler.handleError(it.error)
            }
        }

        compositeDisposable.add(
            viewModel.getShows()
                .subscribe({
                    adapter.submitData(lifecycle, it)
                }, {})
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}