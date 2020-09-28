package com.example.tvshows.dagger.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tvshows.dagger.MyViewModelFactory
import com.example.tvshows.dagger.ViewModelKey
import com.example.tvshows.ui.showslist.ConfigurationViewModel
import com.example.tvshows.ui.showslist.ShowDetailsViewModel
import com.example.tvshows.ui.showslist.ShowsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton


@Module
abstract class ViewModelModule {

    @Singleton
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: MyViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ConfigurationViewModel::class)
    abstract fun bindConfigurationViewModel(viewModel: ConfigurationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShowsViewModel::class)
    abstract fun bindShowsViewModel(viewModel: ShowsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShowDetailsViewModel::class)
    abstract fun bindShowDetailsViewModel(viewModel: ShowDetailsViewModel): ViewModel
}
