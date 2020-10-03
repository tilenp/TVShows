package com.example.tvshows.dagger.module

import android.content.Context
import com.example.tvshows.utilities.ErrorHandler
import com.example.tvshows.utilities.RuntimeSchedulerProvider
import com.example.tvshows.utilities.SchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Singleton
    @Provides
    fun providesSchedulerProvider(): SchedulerProvider {
        return RuntimeSchedulerProvider()
    }

    @Singleton
    @Provides
    fun providesErrorHandler(context: Context): ErrorHandler {
        return ErrorHandler(context)
    }
}