package com.example.tvshows.dagger.module

import com.example.tvshows.network.serviceimpl.ShowSummariesWrapperServiceImpl
import com.example.tvshows.repository.service.ShowSummariesWrapperService
import dagger.Binds
import dagger.Module

@Module
interface ServiceModule {

    @Binds
    fun bindShowSummariesWrapperService(showSummariesWrapperServiceImpl: ShowSummariesWrapperServiceImpl): ShowSummariesWrapperService
}