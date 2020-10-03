package com.example.tvshows.dagger.module

import androidx.lifecycle.ViewModelProvider
import com.example.tvshows.dagger.TestMyViewModelFactory
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class TestViewModelModule {

    @Singleton
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: TestMyViewModelFactory): ViewModelProvider.Factory
}