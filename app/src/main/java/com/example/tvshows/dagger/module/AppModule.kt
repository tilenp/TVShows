package com.example.tvshows.dagger.module

import android.content.Context
import com.example.tvshows.R
import com.example.tvshows.utilities.*
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun providesSchedulerProvider(): SchedulerProvider {
        return RuntimeSchedulerProvider()
    }

    @Singleton
    @Provides
    @Named(PAGE_SIZE)
    fun providesPageSize(context: Context): Int {
        return context.resources.getInteger(R.integer.page_size)
    }

    @Singleton
    @Provides
    @Named(MAX_SIZE)
    fun providesMaxSize(context: Context): Int {
        return context.resources.getInteger(R.integer.max_size)
    }

    @Singleton
    @Provides
    @Named(PREFETCH_DISTANCE)
    fun providesPrefetchDistance(context: Context): Int {
        return context.resources.getInteger(R.integer.prefetch_distance)
    }

    @Singleton
    @Provides
    @Named(INITIAL_LOAD_SIZE)
    fun providesInitialLoadSize(context: Context): Int {
        return context.resources.getInteger(R.integer.initial_load_size)
    }

    @Singleton
    @Provides
    @Named(UI_STATE_INTERVAL)
    fun providesUiStateInterval(): Long {
        return 1000
    }

    @Singleton
    @Provides
    @Named(SPLIT_VIEW)
    fun providesSplitView(context: Context): Boolean {
        return context.resources.getBoolean(R.bool.splitView)
    }
}