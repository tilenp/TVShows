package com.example.tvshows.dagger.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.example.tvshows.dagger.MyViewModelFactory
import com.example.tvshows.dagger.ViewModelKey
import com.example.tvshows.ui.showdetails.ShowDetailsViewModel
import com.example.tvshows.ui.showslist.ShowSummariesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton


@Module
interface ViewModelModule {

    @Singleton
    @Binds
    fun bindViewModelFactory(viewModelFactory: MyViewModelFactory): ViewModelProvider.Factory

    @ExperimentalPagingApi
    @Binds
    @IntoMap
    @ViewModelKey(ShowSummariesViewModel::class)
    fun bindShowsViewModel(viewModel: ShowSummariesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShowDetailsViewModel::class)
    fun bindShowDetailsViewModel(viewModel: ShowDetailsViewModel): ViewModel
}
