package com.example.tvshows.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.example.tvshows.R
import com.example.tvshows.dagger.ComponentProvider
import com.example.tvshows.databinding.ActivityMainBinding
import com.example.tvshows.ui.ConfigurationViewModel.Companion.SHOW_DETAILS_FRAGMENT
import com.example.tvshows.ui.ConfigurationViewModel.Companion.SHOW_SUMMARIES_FRAGMENT
import com.example.tvshows.ui.showdetails.ShowDetailsFragment
import com.example.tvshows.ui.showslist.ShowSummariesFragment
import com.example.tvshows.utilities.isTablet
import com.example.tvshows.utilities.orientation
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@ExperimentalPagingApi
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ConfigurationViewModel
    private val compositeDisposable = CompositeDisposable()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as ComponentProvider).provideAppComponent().inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setUpViewModel()
        setInitialFragment()
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(ConfigurationViewModel::class.java)
    }

    private fun setInitialFragment() {
        val fragment = supportFragmentManager.findFragmentByTag(SHOW_SUMMARIES_FRAGMENT)
        if (fragment == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, ShowSummariesFragment(), SHOW_SUMMARIES_FRAGMENT)
                .commit()
        }
    }

    override fun onStart() {
        super.onStart()
        initObservers()
        viewModel.initData(
            orientation = orientation(),
            isTablet = isTablet()
        )
    }

    private fun initObservers() {
        compositeDisposable.add(
            viewModel.showDetailsObservable()
                .subscribe({
                    replaceWithDetailsFragment()
                }, {})
        )

        compositeDisposable.add(
            viewModel.popBackStackObservable()
                .subscribe({
                    supportFragmentManager.popBackStack()
                }, {})
        )
    }

    private fun replaceWithDetailsFragment() {
        val fragment = supportFragmentManager.findFragmentByTag(SHOW_DETAILS_FRAGMENT) ?: ShowDetailsFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment, SHOW_DETAILS_FRAGMENT)
            .addToBackStack(null)
            .commit()
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}