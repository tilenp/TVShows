package com.example.tvshows.ui.showslist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.tvshows.R
import com.example.tvshows.dagger.MyApplication
import com.example.tvshows.databinding.ActivityMainBinding
import com.example.tvshows.ui.showslist.ConfigurationViewModel.Companion.SHOWS_FRAGMENT
import com.example.tvshows.ui.showslist.ConfigurationViewModel.Companion.SHOW_DETAILS_FRAGMENT
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ConfigurationViewModel
    private val compositeDisposable = CompositeDisposable()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as MyApplication).appComponent.inject(this)
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
        val fragment = supportFragmentManager.findFragmentByTag(SHOWS_FRAGMENT)
        if (fragment == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, ShowsFragment(), SHOWS_FRAGMENT)
                .commit()
        }
    }

    override fun onStart() {
        super.onStart()
        initObservers()
        viewModel.setOrientation(resources.configuration.orientation)
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