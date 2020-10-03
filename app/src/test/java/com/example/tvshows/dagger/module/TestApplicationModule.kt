package com.example.tvshows.dagger.module

import android.content.Context
import com.example.tvshows.utilities.ErrorHandler
import com.example.tvshows.utilities.SchedulerProvider
import com.example.tvshows.utilities.TestSchedulerProvider
import com.nhaarman.mockitokotlin2.mock
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestApplicationModule {

    @Singleton
    @Provides
    fun providesSchedulerProvider(): SchedulerProvider {
        return TestSchedulerProvider()
    }

    @Singleton
    @Provides
    fun providesErrorHandler(context: Context): ErrorHandler {
        return mock()
    }
}