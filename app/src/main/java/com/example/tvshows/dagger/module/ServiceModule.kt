package com.example.tvshows.dagger.module

import com.example.tvshows.network.serviceimpl.ShowDetailsServiceImpl
import com.example.tvshows.network.serviceimpl.ShowSummariesWrapperServiceImpl
import com.example.tvshows.repository.service.ShowDetailsService
import com.example.tvshows.repository.service.ShowSummariesWrapperService
import dagger.Binds
import dagger.Module

@Module
interface ServiceModule {

    @Binds
    fun bindShowSummariesWrapperService(showSummariesWrapperServiceImpl: ShowSummariesWrapperServiceImpl): ShowSummariesWrapperService

    @Binds
    fun bindShowDetailsService(showDetailsServiceImpl: ShowDetailsServiceImpl): ShowDetailsService
}