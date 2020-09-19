package com.example.tvshows.dagger.module

import com.example.tvshows.network.api.ShowsApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApiModule {

    @Singleton
    @Provides
    fun providesShowsApi(): ShowsApi {
        return ShowsApi.create()
    }
}