package com.example.tvshows.dagger.module

import com.example.tvshows.network.serviceimpl.ShowsServiceImpl
import com.example.tvshows.repository.service.ShowsService
import dagger.Binds
import dagger.Module

@Module
interface ServiceModule {

    @Binds
    fun bindShowsService(showsServiceImpl: ShowsServiceImpl): ShowsService
}