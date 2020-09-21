package com.example.tvshows.ui.showslist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tvshows.R
import com.example.tvshows.dagger.MyApplication
import com.example.tvshows.databinding.ActivityMainBinding
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
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
        binding.showsRecyclerView.layoutManager  = GridLayoutManager(this, numberOfColumns)
        binding.showsRecyclerView.adapter = adapter
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