package com.example.tvshows.dagger.module

import com.example.tvshows.database.model.Show
import com.example.tvshows.network.mapper.Mapper
import com.example.tvshows.network.mapper.ShowMapper
import com.example.tvshows.network.mapper.ShowsMapper
import com.example.tvshows.network.remoteModel.RemoteWrapper
import com.example.tvshows.network.remoteModel.RemoteShow
import com.example.tvshows.repository.paging.ShowsWrapper
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

    @Singleton
    @Provides
    fun providesShowsMapper(showMapper: Mapper<RemoteShow, Show>): Mapper<RemoteWrapper<List<RemoteShow>>, ShowsWrapper> {
        return ShowsMapper(showMapper)
    }
}