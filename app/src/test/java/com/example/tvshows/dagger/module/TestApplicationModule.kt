package com.example.tvshows.dagger.module

import com.example.tvshows.utilities.SchedulerProvider
import com.example.tvshows.utilities.TestSchedulerProvider
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
}