package com.example.tvshows.dagger.module

import com.example.tvshows.database.model.Show
import com.example.tvshows.network.mapper.Mapper
import com.example.tvshows.network.mapper.ShowMapper
import com.example.tvshows.network.remoteModel.RemoteShow
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MapperModule {

    @Singleton
    @Provides
    fun providesShowMapper(): Mapper<RemoteShow, Show> {
        return ShowMapper()
    }
}